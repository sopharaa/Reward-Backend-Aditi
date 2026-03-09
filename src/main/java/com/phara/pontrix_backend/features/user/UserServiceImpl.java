package com.phara.pontrix_backend.features.user;

import com.phara.pontrix_backend.domain.Company;
import com.phara.pontrix_backend.domain.User;
import com.phara.pontrix_backend.features.auth.JwtService;
import com.phara.pontrix_backend.features.auth.TokenBlacklistService;
import com.phara.pontrix_backend.features.companies.CompanyRepository;
import com.phara.pontrix_backend.features.user.dto.UpdateUserProfileRequest;
import com.phara.pontrix_backend.features.user.dto.UserLoginRequest;
import com.phara.pontrix_backend.features.user.dto.UserLoginResponse;
import com.phara.pontrix_backend.features.user.dto.UserProfileResponse;
import com.phara.pontrix_backend.features.user.dto.UserRegisterRequest;
import com.phara.pontrix_backend.features.user.dto.UserRegisterResponse;
import com.phara.pontrix_backend.service.CloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final CloudStorageService cloudStorageService;

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        Company company = companyRepository.findByIdAndDeletedAtIsNull(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (userRepository.existsByEmailAndDeletedAtIsNull(request.email())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setCompany(company);
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        User saved = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(saved.getEmail(), "USER");
        String refreshToken = jwtService.generateRefreshToken(saved.getEmail(), "USER");

        return new UserRegisterResponse(
                saved.getId(),
                company.getId(),
                company.getName(),
                saved.getName(),
                saved.getEmail(),
                saved.getPoints(),
                accessToken,
                refreshToken
        );
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail(), "USER");
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), "USER");

        return new UserLoginResponse(
                user.getId(),
                user.getCompany() != null ? user.getCompany().getId() : null,
                user.getCompany() != null ? user.getCompany().getName() : null,
                user.getName(),
                user.getEmail(),
                user.getPoints(),
                user.getProfileImage(),
                accessToken,
                refreshToken
        );
    }

    @Override
    public void logout(String token) {
        tokenBlacklistService.blacklist(token);
    }

    @Override
    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateProfile(String email, UpdateUserProfileRequest request, MultipartFile profileImage) {
        User user = userRepository.findByEmail(email)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }

        if (request.password() != null && !request.password().isBlank()) {
            if (!request.password().equals(request.confirmPassword())) {
                throw new RuntimeException("Passwords do not match");
            }
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            // Delete old image if exists
            if (user.getProfileImage() != null && !user.getProfileImage().isBlank()) {
                cloudStorageService.deleteFile(user.getProfileImage());
            }
            String imageUrl = cloudStorageService.uploadFile(profileImage, "users");
            user.setProfileImage(imageUrl);
        }

        return toProfileResponse(userRepository.save(user));
    }

    private UserProfileResponse toProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getCompany() != null ? user.getCompany().getId() : null,
                user.getCompany() != null ? user.getCompany().getName() : null,
                user.getName(),
                user.getEmail(),
                user.getPoints(),
                user.getProfileImage(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
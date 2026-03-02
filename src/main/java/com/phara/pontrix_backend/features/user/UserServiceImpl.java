package com.phara.pontrix_backend.features.user;

import com.phara.pontrix_backend.domain.Company;
import com.phara.pontrix_backend.domain.User;
import com.phara.pontrix_backend.features.auth.JwtService;
import com.phara.pontrix_backend.features.companies.CompanyRepository;
import com.phara.pontrix_backend.features.user.dto.UserLoginRequest;
import com.phara.pontrix_backend.features.user.dto.UserLoginResponse;
import com.phara.pontrix_backend.features.user.dto.UserRegisterRequest;
import com.phara.pontrix_backend.features.user.dto.UserRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        Company company = companyRepository.findByIdAndDeletedAtIsNull(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (userRepository.existsByEmail(request.email())) {
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
}
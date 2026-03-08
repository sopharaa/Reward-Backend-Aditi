package com.phara.pontrix_backend.features.staff;

import com.phara.pontrix_backend.domain.Staff;
import com.phara.pontrix_backend.features.auth.JwtService;
import com.phara.pontrix_backend.features.auth.TokenBlacklistService;
import com.phara.pontrix_backend.features.staff.dto.StaffLoginRequest;
import com.phara.pontrix_backend.features.staff.dto.StaffLoginResponse;
import com.phara.pontrix_backend.features.staff.dto.StaffProfileResponse;
import com.phara.pontrix_backend.features.staff.dto.UpdateStaffProfileRequest;
import com.phara.pontrix_backend.features.user.UserRepository;
import com.phara.pontrix_backend.features.user.dto.UserProfileResponse;
import com.phara.pontrix_backend.service.CloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final CloudStorageService cloudStorageService;

    @Override
    public StaffLoginResponse login(StaffLoginRequest request) {
        Staff staff = staffRepository.findByEmail(request.email())
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (!passwordEncoder.matches(request.password(), staff.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtService.generateAccessToken(staff.getEmail(), "STAFF");
        String refreshToken = jwtService.generateRefreshToken(staff.getEmail(), "STAFF");

        return new StaffLoginResponse(
                staff.getId(),
                staff.getCompany() != null ? staff.getCompany().getId() : null,
                staff.getName(),
                staff.getEmail(),
                staff.getProfileImage(),
                accessToken,
                refreshToken
        );
    }

    @Override
    public void logout(String token) {
        tokenBlacklistService.blacklist(token);
    }

    @Override
    public StaffProfileResponse getProfile(String email) {
        Staff staff = staffRepository.findByEmail(email)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        return toProfileResponse(staff);
    }

    @Override
    public StaffProfileResponse updateProfile(String email, UpdateStaffProfileRequest request, MultipartFile profileImage) {
        Staff staff = staffRepository.findByEmail(email)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (request.name() != null && !request.name().isBlank()) {
            staff.setName(request.name());
        }

        if (request.password() != null && !request.password().isBlank()) {
            if (!request.password().equals(request.confirmPassword())) {
                throw new RuntimeException("Passwords do not match");
            }
            staff.setPassword(passwordEncoder.encode(request.password()));
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            if (staff.getProfileImage() != null && !staff.getProfileImage().isBlank()) {
                cloudStorageService.deleteFile(staff.getProfileImage());
            }
            String imageUrl = cloudStorageService.uploadFile(profileImage, "staff");
            staff.setProfileImage(imageUrl);
        }

        return toProfileResponse(staffRepository.save(staff));
    }

    @Override
    public List<UserProfileResponse> getCompanyUsers(String staffEmail) {
        Staff staff = staffRepository.findByEmail(staffEmail)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (staff.getCompany() == null) {
            return List.of();
        }

        return userRepository.findByCompanyIdAndDeletedAtIsNull(staff.getCompany().getId())
                .stream()
                .map(u -> new UserProfileResponse(
                        u.getId(),
                        u.getCompany() != null ? u.getCompany().getId() : null,
                        u.getCompany() != null ? u.getCompany().getName() : null,
                        u.getName(),
                        u.getEmail(),
                        u.getPoints(),
                        u.getProfileImage(),
                        u.getCreatedAt(),
                        u.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    private StaffProfileResponse toProfileResponse(Staff staff) {
        return new StaffProfileResponse(
                staff.getId(),
                staff.getCompany() != null ? staff.getCompany().getId() : null,
                staff.getCompany() != null ? staff.getCompany().getName() : null,
                staff.getName(),
                staff.getEmail(),
                staff.getProfileImage(),
                staff.getCreatedAt(),
                staff.getUpdatedAt()
        );
    }
}

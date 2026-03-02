package com.phara.pontrix_backend.features.staff;

import com.phara.pontrix_backend.domain.Staff;
import com.phara.pontrix_backend.features.auth.JwtService;
import com.phara.pontrix_backend.features.auth.TokenBlacklistService;
import com.phara.pontrix_backend.features.staff.dto.StaffLoginRequest;
import com.phara.pontrix_backend.features.staff.dto.StaffLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

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
}


package com.phara.pontrix_backend.features.staff.dto;

public record StaffLoginResponse(
        Long id,
        String name,
        String email,
        String profileImage,
        String accessToken,
        String refreshToken
) {
}


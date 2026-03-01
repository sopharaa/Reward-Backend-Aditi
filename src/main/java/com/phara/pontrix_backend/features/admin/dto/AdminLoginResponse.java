package com.phara.pontrix_backend.features.admin.dto;

public record AdminLoginResponse(
        Long id,
        String name,
        String profileImage,
        String accessToken,
        String refreshToken
) {
}

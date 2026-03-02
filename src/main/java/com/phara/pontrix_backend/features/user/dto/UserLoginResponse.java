package com.phara.pontrix_backend.features.user.dto;

public record UserLoginResponse(
        Long id,
        Long companyId,
        String companyName,
        String name,
        String email,
        Long points,
        String profileImage,
        String accessToken,
        String refreshToken
) {
}
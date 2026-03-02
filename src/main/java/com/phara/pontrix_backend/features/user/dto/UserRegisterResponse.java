package com.phara.pontrix_backend.features.user.dto;

public record UserRegisterResponse(
        Long id,
        Long companyId,
        String companyName,
        String name,
        String email,
        Long points,
        String accessToken,
        String refreshToken
) {
}
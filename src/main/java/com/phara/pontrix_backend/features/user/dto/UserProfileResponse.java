package com.phara.pontrix_backend.features.user.dto;

import java.time.LocalDateTime;

public record UserProfileResponse(
        Long id,
        Long companyId,
        String companyName,
        String name,
        String email,
        Long points,
        String profileImage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}


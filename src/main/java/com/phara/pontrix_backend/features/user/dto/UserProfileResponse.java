package com.phara.pontrix_backend.features.user.dto;

import java.time.OffsetDateTime;

public record UserProfileResponse(
        Long id,
        Long companyId,
        String companyName,
        String name,
        String email,
        Long points,
        String profileImage,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

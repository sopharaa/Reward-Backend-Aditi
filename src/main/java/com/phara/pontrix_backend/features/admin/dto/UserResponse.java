package com.phara.pontrix_backend.features.admin.dto;

import java.time.OffsetDateTime;

public record UserResponse(
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

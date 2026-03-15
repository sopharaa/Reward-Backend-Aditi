package com.phara.pontrix_backend.features.admin.dto;

import java.time.OffsetDateTime;

public record StaffResponse(
        Long id,
        Long companyId,
        String companyName,
        String name,
        String email,
        String profileImage,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

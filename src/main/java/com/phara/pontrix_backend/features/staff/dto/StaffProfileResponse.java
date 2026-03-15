package com.phara.pontrix_backend.features.staff.dto;

import java.time.OffsetDateTime;

public record StaffProfileResponse(
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

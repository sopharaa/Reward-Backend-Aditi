package com.phara.pontrix_backend.features.staff.dto;

import java.time.LocalDateTime;

public record StaffProfileResponse(
        Long id,
        Long companyId,
        String companyName,
        String name,
        String email,
        String profileImage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}


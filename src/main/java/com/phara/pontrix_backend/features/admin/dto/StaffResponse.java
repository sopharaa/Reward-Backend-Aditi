package com.phara.pontrix_backend.features.admin.dto;

import java.time.LocalDateTime;

public record StaffResponse(
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


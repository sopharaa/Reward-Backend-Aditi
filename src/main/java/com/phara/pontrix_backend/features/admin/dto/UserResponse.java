package com.phara.pontrix_backend.features.admin.dto;

import java.time.LocalDateTime;

public record UserResponse(
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


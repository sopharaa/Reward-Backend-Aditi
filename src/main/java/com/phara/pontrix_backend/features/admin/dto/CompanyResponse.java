package com.phara.pontrix_backend.features.admin.dto;

import java.time.LocalDateTime;

public record CompanyResponse(
        Long id,
        String name,
        String type,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}



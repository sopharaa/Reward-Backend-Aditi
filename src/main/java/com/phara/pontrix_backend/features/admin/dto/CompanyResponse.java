package com.phara.pontrix_backend.features.admin.dto;

import java.time.OffsetDateTime;

public record CompanyResponse(
        Long id,
        String name,
        String type,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

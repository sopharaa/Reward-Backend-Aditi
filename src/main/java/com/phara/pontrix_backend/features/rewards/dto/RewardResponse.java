package com.phara.pontrix_backend.features.rewards.dto;

import java.time.OffsetDateTime;

public record RewardResponse(
        Long id,
        Long companyId,
        String companyName,
        String name,
        String description,
        Integer stock,
        Integer pointRequired,
        String image,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

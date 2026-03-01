package com.phara.pontrix_backend.features.rewards.dto;

import java.time.LocalDateTime;

public record RewardResponse(
        Long id,
        Long companyId,
        String companyName,
        String name,
        String description,
        Integer stock,
        Integer pointRequired,
        String image,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}


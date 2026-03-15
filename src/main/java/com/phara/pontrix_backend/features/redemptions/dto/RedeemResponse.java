package com.phara.pontrix_backend.features.redemptions.dto;

import java.time.OffsetDateTime;

public record RedeemResponse(
        Long id,
        Long userId,
        String userName,
        Long staffId,
        String staffName,
        Long rewardId,
        String rewardName,
        Integer pointSpend,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}


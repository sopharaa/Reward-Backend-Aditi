package com.phara.pontrix_backend.features.redemptions.dto;

import java.time.LocalDateTime;

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
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}


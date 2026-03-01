package com.phara.pontrix_backend.features.rewards.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRewardRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotBlank(message = "Reward name is required")
        String name,

        String description,

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock must be 0 or greater")
        Integer stock,

        @NotNull(message = "Point required is required")
        @Min(value = 1, message = "Point required must be at least 1")
        Integer pointRequired
) {
}


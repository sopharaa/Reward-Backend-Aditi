package com.phara.pontrix_backend.features.admin.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCompanyRequest(
        @NotBlank(message = "Company name is required")
        String name,
        @NotBlank(message = "Company type is required")
        String type,
        String description
) {
}



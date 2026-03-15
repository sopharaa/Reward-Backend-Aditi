package com.phara.pontrix_backend.features.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateStaffRequest(
        @NotBlank(message = "Staff name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        String password
) {
}

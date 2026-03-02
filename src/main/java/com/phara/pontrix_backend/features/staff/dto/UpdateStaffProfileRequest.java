package com.phara.pontrix_backend.features.staff.dto;

import jakarta.validation.constraints.AssertTrue;

public record UpdateStaffProfileRequest(
        String name,
        String password,
        String confirmPassword
) {
    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordMatching() {
        if (password == null || password.isBlank()) return true;
        return password.equals(confirmPassword);
    }
}
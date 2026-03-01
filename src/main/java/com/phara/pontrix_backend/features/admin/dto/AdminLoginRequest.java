package com.phara.pontrix_backend.features.admin.dto;

public record AdminLoginRequest(
        String name,
        String password
) {
}

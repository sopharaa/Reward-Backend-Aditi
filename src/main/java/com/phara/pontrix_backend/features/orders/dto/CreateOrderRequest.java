package com.phara.pontrix_backend.features.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Total amount is required")
        @Min(value = 0, message = "Total amount must be 0 or greater")
        Double totalAmount,

        List<OrderItemDto> orderItems,

        String note
) {
    public record OrderItemDto(String name, Double price) {}
}

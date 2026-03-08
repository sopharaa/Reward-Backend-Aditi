package com.phara.pontrix_backend.features.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long staffId,
        String staffName,
        Long userId,
        String userName,
        Double totalAmount,
        Long pointsEarned,
        List<CreateOrderRequest.OrderItemDto> orderItems,
        String note,
        LocalDateTime createdAt
) {
}

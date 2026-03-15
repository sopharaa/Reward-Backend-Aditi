package com.phara.pontrix_backend.features.orders.dto;

import java.time.OffsetDateTime;
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
        OffsetDateTime createdAt
) {
}

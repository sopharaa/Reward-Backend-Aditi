package com.phara.pontrix_backend.features.orders;

import com.phara.pontrix_backend.features.orders.dto.CreateOrderRequest;
import com.phara.pontrix_backend.features.orders.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(String staffEmail, CreateOrderRequest request);
    List<OrderResponse> getMyOrders(String staffEmail);
    List<OrderResponse> getOrdersByUser(String userEmail);
}


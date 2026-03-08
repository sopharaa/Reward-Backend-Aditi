package com.phara.pontrix_backend.features.orders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phara.pontrix_backend.domain.Order;
import com.phara.pontrix_backend.domain.Staff;
import com.phara.pontrix_backend.domain.User;
import com.phara.pontrix_backend.features.orders.dto.CreateOrderRequest;
import com.phara.pontrix_backend.features.orders.dto.OrderResponse;
import com.phara.pontrix_backend.features.staff.StaffRepository;
import com.phara.pontrix_backend.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    // 1 point per 10 units of currency spent (e.g., $10 = 1 point)
    private static final double POINTS_RATE = 0.1;

    @Override
    @Transactional
    public OrderResponse createOrder(String staffEmail, CreateOrderRequest request) {
        Staff staff = staffRepository.findByEmail(staffEmail)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        User user = userRepository.findByIdAndDeletedAtIsNull(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate user belongs to the same company as staff
        if (staff.getCompany() != null && user.getCompany() != null) {
            if (!staff.getCompany().getId().equals(user.getCompany().getId())) {
                throw new RuntimeException("User does not belong to your company");
            }
        }

        long pointsEarned = Math.round(request.totalAmount() * POINTS_RATE);

        // Serialize orderItems to JSON string stored in note
        String itemsJson = null;
        if (request.orderItems() != null && !request.orderItems().isEmpty()) {
            try {
                itemsJson = objectMapper.writeValueAsString(request.orderItems());
            } catch (Exception e) {
                itemsJson = "[]";
            }
        }

        Order order = new Order();
        order.setStaff(staff);
        order.setUser(user);
        order.setTotalAmount(request.totalAmount());
        order.setPointsEarned(pointsEarned);
        order.setNote(itemsJson != null ? itemsJson : (request.note() != null ? request.note() : ""));

        // Add points to user
        user.setPoints(user.getPoints() + pointsEarned);
        userRepository.save(user);

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    @Override
    public List<OrderResponse> getMyOrders(String staffEmail) {
        Staff staff = staffRepository.findByEmail(staffEmail)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        return orderRepository.findByStaffId(staff.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private List<CreateOrderRequest.OrderItemDto> parseItems(String note) {
        if (note == null || note.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(note, new TypeReference<List<CreateOrderRequest.OrderItemDto>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private OrderResponse toResponse(Order order) {
        List<CreateOrderRequest.OrderItemDto> items = parseItems(order.getNote());
        return new OrderResponse(
                order.getId(),
                order.getStaff().getId(),
                order.getStaff().getName(),
                order.getUser().getId(),
                order.getUser().getName(),
                order.getTotalAmount(),
                order.getPointsEarned(),
                items,
                order.getNote(),
                order.getCreatedAt()
        );
    }
}

package com.phara.pontrix_backend.features.staff;

import com.phara.pontrix_backend.features.orders.OrderService;
import com.phara.pontrix_backend.features.orders.dto.CreateOrderRequest;
import com.phara.pontrix_backend.features.orders.dto.OrderResponse;
import com.phara.pontrix_backend.features.redemptions.RedeemService;
import com.phara.pontrix_backend.features.redemptions.dto.RedeemResponse;
import com.phara.pontrix_backend.features.rewards.dto.RewardResponse;
import com.phara.pontrix_backend.features.staff.dto.StaffLoginRequest;
import com.phara.pontrix_backend.features.staff.dto.StaffLoginResponse;
import com.phara.pontrix_backend.features.staff.dto.StaffProfileResponse;
import com.phara.pontrix_backend.features.staff.dto.UpdateStaffProfileRequest;
import com.phara.pontrix_backend.features.user.dto.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
@CrossOrigin
public class StaffController {

    private final StaffService staffService;
    private final OrderService orderService;
    private final RedeemService redeemService;

    @PostMapping("/login")
    public ResponseEntity<StaffLoginResponse> login(@Valid @RequestBody StaffLoginRequest request) {
        StaffLoginResponse response = staffService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        staffService.logout(token);
        return ResponseEntity.ok("{\"message\":\"Logged out successfully\"}");
    }

    @GetMapping("/profile")
    public ResponseEntity<StaffProfileResponse> getProfile(Principal principal) {
        StaffProfileResponse response = staffService.getProfile(principal.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(
            Principal principal,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        StaffProfileResponse response = staffService.updateProfile(
                principal.getName(),
                new UpdateStaffProfileRequest(name, password, confirmPassword),
                profileImage
        );
        return ResponseEntity.ok(new ApiResponse("Staff profile updated successfully", response));
    }

    @GetMapping("/company-users")
    public ResponseEntity<List<UserProfileResponse>> getCompanyUsers(Principal principal) {
        List<UserProfileResponse> users = staffService.getCompanyUsers(principal.getName());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(
            Principal principal,
            @Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(principal.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Principal principal) {
        List<OrderResponse> response = orderService.getMyOrders(principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rewards")
    public ResponseEntity<List<RewardResponse>> getCompanyRewards(Principal principal) {
        List<RewardResponse> response = staffService.getCompanyRewards(principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company-transactions")
    public ResponseEntity<List<RedeemResponse>> getCompanyTransactions(Principal principal) {
        List<RedeemResponse> response = redeemService.getCompanyRedemptions(principal.getName());
        return ResponseEntity.ok(response);
    }

    record ApiResponse(String message, Object data) {}
}

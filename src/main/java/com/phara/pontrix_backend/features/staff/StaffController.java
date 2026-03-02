package com.phara.pontrix_backend.features.staff;

import com.phara.pontrix_backend.features.staff.dto.StaffLoginRequest;
import com.phara.pontrix_backend.features.staff.dto.StaffLoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
@CrossOrigin
public class StaffController {

    private final StaffService staffService;

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
}


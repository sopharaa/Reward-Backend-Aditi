package com.phara.pontrix_backend.features.staff;

import com.phara.pontrix_backend.features.staff.dto.StaffLoginRequest;
import com.phara.pontrix_backend.features.staff.dto.StaffLoginResponse;
import com.phara.pontrix_backend.features.staff.dto.StaffProfileResponse;
import com.phara.pontrix_backend.features.staff.dto.UpdateStaffProfileRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

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

    record ApiResponse(String message, Object data) {}
}

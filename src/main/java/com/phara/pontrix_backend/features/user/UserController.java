package com.phara.pontrix_backend.features.user;

import com.phara.pontrix_backend.features.user.dto.UpdateUserProfileRequest;
import com.phara.pontrix_backend.features.user.dto.UserLoginRequest;
import com.phara.pontrix_backend.features.user.dto.UserLoginResponse;
import com.phara.pontrix_backend.features.user.dto.UserProfileResponse;
import com.phara.pontrix_backend.features.user.dto.UserRegisterRequest;
import com.phara.pontrix_backend.features.user.dto.UserRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserRegisterResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        UserLoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        userService.logout(token);
        return ResponseEntity.ok("{\"message\":\"Logged out successfully\"}");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Principal principal) {
        UserProfileResponse response = userService.getProfile(principal.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileResponse> updateProfile(
            Principal principal,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        UserProfileResponse response = userService.updateProfile(
                principal.getName(),
                new UpdateUserProfileRequest(name, password, confirmPassword),
                profileImage
        );
        return ResponseEntity.ok(response);
    }
}
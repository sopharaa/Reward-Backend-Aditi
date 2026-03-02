package com.phara.pontrix_backend.features.admin;

import com.phara.pontrix_backend.features.admin.dto.*;
import com.phara.pontrix_backend.features.rewards.dto.CreateRewardRequest;
import com.phara.pontrix_backend.features.rewards.dto.RewardResponse;
import com.phara.pontrix_backend.features.rewards.dto.UpdateRewardRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@CrossOrigin
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    public AdminLoginResponse login(@RequestBody AdminLoginRequest request) {
        return adminService.login(request);
    }

    // Company Management Endpoints
    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        CompanyResponse response = adminService.createCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse("Company created successfully", response)
        );
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<?> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCompanyRequest request) {
        CompanyResponse response = adminService.updateCompany(id, request);
        return ResponseEntity.ok(
            new ApiResponse("Company updated successfully", response)
        );
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<CompanyResponse> viewCompany(@PathVariable Long id) {
        CompanyResponse response = adminService.viewCompany(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/companies")
    public ResponseEntity<List<CompanyResponse>> viewAllCompanies() {
        List<CompanyResponse> response = adminService.viewAllCompanies();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        adminService.deleteCompany(id);
        return ResponseEntity.ok(
            new ApiResponse("Company deleted successfully", null)
        );
    }

    // Staff Management Endpoints
    @PostMapping("/staff")
    public ResponseEntity<?> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        StaffResponse response = adminService.createStaff(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse("Staff created successfully", response)
        );
    }

    @PutMapping("/staff/{id}")
    public ResponseEntity<?> updateStaff(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStaffRequest request) {
        StaffResponse response = adminService.updateStaff(id, request);
        return ResponseEntity.ok(
            new ApiResponse("Staff updated successfully", response)
        );
    }

    @GetMapping("/staff/{id}")
    public ResponseEntity<StaffResponse> viewStaff(@PathVariable Long id) {
        StaffResponse response = adminService.viewStaff(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/staff")
    public ResponseEntity<List<StaffResponse>> viewAllStaff() {
        List<StaffResponse> response = adminService.viewAllStaff();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/staff/company/{companyId}")
    public ResponseEntity<List<StaffResponse>> viewStaffByCompany(@PathVariable Long companyId) {
        List<StaffResponse> response = adminService.viewStaffByCompany(companyId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/staff/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long id) {
        adminService.deleteStaff(id);
        return ResponseEntity.ok(
            new ApiResponse("Staff deleted successfully", null)
        );
    }

    // Reward Management Endpoints
    @PostMapping(value = "/rewards", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReward(
            @Valid @ModelAttribute CreateRewardRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        RewardResponse response = adminService.createReward(request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse("Reward created successfully", response)
        );
    }

    @PutMapping(value = "/rewards/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateReward(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateRewardRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        RewardResponse response = adminService.updateReward(id, request, image);
        return ResponseEntity.ok(
            new ApiResponse("Reward updated successfully", response)
        );
    }

    @GetMapping("/rewards/{id}")
    public ResponseEntity<RewardResponse> viewReward(@PathVariable Long id) {
        RewardResponse response = adminService.viewReward(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rewards")
    public ResponseEntity<List<RewardResponse>> viewAllRewards() {
        List<RewardResponse> response = adminService.viewAllRewards();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rewards/company/{companyId}")
    public ResponseEntity<List<RewardResponse>> viewRewardsByCompany(@PathVariable Long companyId) {
        List<RewardResponse> response = adminService.viewRewardsByCompany(companyId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/rewards/{id}")
    public ResponseEntity<?> deleteReward(@PathVariable Long id) {
        adminService.deleteReward(id);
        return ResponseEntity.ok(
            new ApiResponse("Reward deleted successfully", null)
        );
    }

    // User Management Endpoints
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = adminService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse("User created successfully", response)
        );
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = adminService.updateUser(id, request);
        return ResponseEntity.ok(
            new ApiResponse("User updated successfully", response)
        );
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> viewUser(@PathVariable Long id) {
        UserResponse response = adminService.viewUser(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> viewAllUsers() {
        List<UserResponse> response = adminService.viewAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/company/{companyId}")
    public ResponseEntity<List<UserResponse>> viewUsersByCompany(@PathVariable Long companyId) {
        List<UserResponse> response = adminService.viewUsersByCompany(companyId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(
            new ApiResponse("User deleted successfully", null)
        );
    }

    // Inner class for API response with message
    record ApiResponse(String message, Object data) {}
}
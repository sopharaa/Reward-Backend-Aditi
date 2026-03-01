package com.phara.pontrix_backend.features.admin;

import com.phara.pontrix_backend.features.admin.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Inner class for API response with message
    record ApiResponse(String message, Object data) {}
}
package com.phara.pontrix_backend.features.admin;

import com.phara.pontrix_backend.features.admin.dto.*;
import com.phara.pontrix_backend.features.rewards.dto.CreateRewardRequest;
import com.phara.pontrix_backend.features.rewards.dto.RewardResponse;
import com.phara.pontrix_backend.features.rewards.dto.UpdateRewardRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminService {
    AdminLoginResponse login(AdminLoginRequest request);
    void logout(String token);

    // Company Management
    CompanyResponse createCompany(CreateCompanyRequest request);
    CompanyResponse updateCompany(Long id, UpdateCompanyRequest request);
    CompanyResponse viewCompany(Long id);
    List<CompanyResponse> viewAllCompanies();
    void deleteCompany(Long id);

    // Staff Management
    StaffResponse createStaff(CreateStaffRequest request);
    StaffResponse updateStaff(Long id, UpdateStaffRequest request);
    StaffResponse viewStaff(Long id);
    List<StaffResponse> viewAllStaff();
    List<StaffResponse> viewStaffByCompany(Long companyId);
    void deleteStaff(Long id);

    // Reward Management
    RewardResponse createReward(CreateRewardRequest request, MultipartFile image);
    RewardResponse updateReward(Long id, UpdateRewardRequest request, MultipartFile image);
    RewardResponse viewReward(Long id);
    List<RewardResponse> viewAllRewards();
    List<RewardResponse> viewRewardsByCompany(Long companyId);
    void deleteReward(Long id);

    // User Management
    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(Long id, UpdateUserRequest request);
    UserResponse viewUser(Long id);
    List<UserResponse> viewAllUsers();
    List<UserResponse> viewUsersByCompany(Long companyId);
    void deleteUser(Long id);
}

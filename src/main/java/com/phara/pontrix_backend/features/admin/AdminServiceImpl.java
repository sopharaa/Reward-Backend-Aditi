package com.phara.pontrix_backend.features.admin;

import com.phara.pontrix_backend.domain.Company;
import com.phara.pontrix_backend.domain.Staff;
import com.phara.pontrix_backend.domain.User;
import com.phara.pontrix_backend.features.auth.JwtService;
import com.phara.pontrix_backend.features.auth.TokenBlacklistService;
import com.phara.pontrix_backend.features.companies.CompanyRepository;
import com.phara.pontrix_backend.features.rewards.RewardService;
import com.phara.pontrix_backend.features.rewards.dto.CreateRewardRequest;
import com.phara.pontrix_backend.features.rewards.dto.RewardResponse;
import com.phara.pontrix_backend.features.rewards.dto.UpdateRewardRequest;
import com.phara.pontrix_backend.features.staff.StaffRepository;
import com.phara.pontrix_backend.features.user.UserRepository;
import com.phara.pontrix_backend.mapper.AdminMapper;
import com.phara.pontrix_backend.mapper.CompanyMapper;
import com.phara.pontrix_backend.mapper.StaffMapper;
import com.phara.pontrix_backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.phara.pontrix_backend.domain.Admin;
import com.phara.pontrix_backend.features.admin.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminMapper adminMapper;
    private final JwtService jwtService;
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final RewardService rewardService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public AdminLoginResponse login(AdminLoginRequest request) {

        Admin admin = adminRepository.findByName(request.name())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(request.password(), admin.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String accessToken = jwtService.generateAccessToken(admin.getName(), "ADMIN");
        String refreshToken = jwtService.generateRefreshToken(admin.getName(), "ADMIN");

        return adminMapper.toLoginResponse(admin, accessToken, refreshToken);
    }

    @Override
    public void logout(String token) {
        tokenBlacklistService.blacklist(token);
    }

    @Override
    public CompanyResponse createCompany(CreateCompanyRequest request) {
        Company company = companyMapper.toEntity(request);
        Company savedCompany = companyRepository.save(company);
        return companyMapper.toResponse(savedCompany);
    }

    @Override
    public CompanyResponse updateCompany(Long id, UpdateCompanyRequest request) {
        Company company = companyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        companyMapper.updateEntity(request, company);
        Company updatedCompany = companyRepository.save(company);
        return companyMapper.toResponse(updatedCompany);
    }

    @Override
    public CompanyResponse viewCompany(Long id) {
        Company company = companyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return companyMapper.toResponse(company);
    }

    @Override
    public List<CompanyResponse> viewAllCompanies() {
        return companyRepository.findByDeletedAtIsNull()
                .stream()
                .map(companyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCompany(Long id) {
        Company company = companyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Soft delete
        company.setDeletedAt(LocalDateTime.now());
        companyRepository.save(company);
    }

    // Staff Management
    @Override
    public StaffResponse createStaff(CreateStaffRequest request) {
        Company company = companyRepository.findByIdAndDeletedAtIsNull(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (staffRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already in use");
        }

        Staff staff = new Staff();
        staff.setCompany(company);
        staff.setName(request.name());
        staff.setEmail(request.email());
        staff.setPassword(passwordEncoder.encode(request.password()));

        return staffMapper.toResponse(staffRepository.save(staff));
    }

    @Override
    public StaffResponse updateStaff(Long id, UpdateStaffRequest request) {
        Staff staff = staffRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        staffMapper.updateEntity(request, staff);

        if (request.companyId() != null) {
            Company company = companyRepository.findById(request.companyId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            staff.setCompany(company);
        }

        if (request.password() != null && !request.password().isBlank()) {
            staff.setPassword(passwordEncoder.encode(request.password()));
        }

        return staffMapper.toResponse(staffRepository.save(staff));
    }

    @Override
    public StaffResponse viewStaff(Long id) {
        Staff staff = staffRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        return staffMapper.toResponse(staff);
    }

    @Override
    public List<StaffResponse> viewAllStaff() {
        return staffRepository.findByDeletedAtIsNull()
                .stream()
                .map(staffMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StaffResponse> viewStaffByCompany(Long companyId) {
        return staffRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
                .stream()
                .map(staffMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStaff(Long id) {
        Staff staff = staffRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        staff.setDeletedAt(LocalDateTime.now());
        staffRepository.save(staff);
    }

    // Reward Management - Delegate to RewardService
    @Override
    public RewardResponse createReward(CreateRewardRequest request, MultipartFile image) {
        return rewardService.createReward(request, image);
    }

    @Override
    public RewardResponse updateReward(Long id, UpdateRewardRequest request, MultipartFile image) {
        return rewardService.updateReward(id, request, image);
    }

    @Override
    public RewardResponse viewReward(Long id) {
        return rewardService.viewReward(id);
    }

    @Override
    public List<RewardResponse> viewAllRewards() {
        return rewardService.viewAllRewards();
    }

    @Override
    public List<RewardResponse> viewRewardsByCompany(Long companyId) {
        return rewardService.viewRewardsByCompany(companyId);
    }

    @Override
    public void deleteReward(Long id) {
        rewardService.deleteReward(id);
    }

    // User Management
    @Override
    public UserResponse createUser(CreateUserRequest request) {
        Company company = companyRepository.findByIdAndDeletedAtIsNull(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (userRepository.existsByEmailAndDeletedAtIsNull(request.email())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setCompany(company);
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateEntity(request, user);

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        if (request.companyId() != null) {
            Company company = companyRepository.findByIdAndDeletedAtIsNull(request.companyId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            user.setCompany(company);
        }

        if (request.points() != null) {
            user.setPoints(request.points());
        }

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse viewUser(Long id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> viewAllUsers() {
        return userRepository.findByDeletedAtIsNull()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> viewUsersByCompany(Long companyId) {
        return userRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}

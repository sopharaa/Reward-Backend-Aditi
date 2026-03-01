package com.phara.pontrix_backend.features.admin;

import com.phara.pontrix_backend.domain.Company;
import com.phara.pontrix_backend.features.auth.JwtService;
import com.phara.pontrix_backend.features.companies.CompanyRepository;
import com.phara.pontrix_backend.features.rewards.RewardService;
import com.phara.pontrix_backend.features.rewards.dto.CreateRewardRequest;
import com.phara.pontrix_backend.features.rewards.dto.RewardResponse;
import com.phara.pontrix_backend.features.rewards.dto.UpdateRewardRequest;
import com.phara.pontrix_backend.mapper.AdminMapper;
import com.phara.pontrix_backend.mapper.CompanyMapper;
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
    private final RewardService rewardService;

    @Override
    public AdminLoginResponse login(AdminLoginRequest request) {

        Admin admin = adminRepository.findByName(request.name())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(request.password(), admin.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String accessToken = jwtService.generateAccessToken(admin.getName());
        String refreshToken = jwtService.generateRefreshToken(admin.getName());

        return adminMapper.toLoginResponse(admin, accessToken, refreshToken);
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
}

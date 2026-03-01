package com.phara.pontrix_backend.features.admin;

import com.phara.pontrix_backend.features.admin.dto.*;

import java.util.List;

public interface AdminService {
    AdminLoginResponse login(AdminLoginRequest request);

    // Company Management
    CompanyResponse createCompany(CreateCompanyRequest request);
    CompanyResponse updateCompany(Long id, UpdateCompanyRequest request);
    CompanyResponse viewCompany(Long id);
    List<CompanyResponse> viewAllCompanies();
    void deleteCompany(Long id);
}

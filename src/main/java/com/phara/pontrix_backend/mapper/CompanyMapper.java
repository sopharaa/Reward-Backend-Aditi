package com.phara.pontrix_backend.mapper;

import com.phara.pontrix_backend.domain.Company;
import com.phara.pontrix_backend.features.admin.dto.CompanyResponse;
import com.phara.pontrix_backend.features.admin.dto.CreateCompanyRequest;
import com.phara.pontrix_backend.features.admin.dto.UpdateCompanyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Company toEntity(CreateCompanyRequest request);

    CompanyResponse toResponse(Company company);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(UpdateCompanyRequest request, @MappingTarget Company company);
}



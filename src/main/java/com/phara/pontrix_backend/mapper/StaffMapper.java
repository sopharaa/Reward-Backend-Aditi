package com.phara.pontrix_backend.mapper;

import com.phara.pontrix_backend.domain.Staff;
import com.phara.pontrix_backend.features.admin.dto.StaffResponse;
import com.phara.pontrix_backend.features.admin.dto.UpdateStaffRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StaffMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    StaffResponse toResponse(Staff staff);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(UpdateStaffRequest request, @MappingTarget Staff staff);
}


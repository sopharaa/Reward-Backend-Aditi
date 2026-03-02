package com.phara.pontrix_backend.mapper;

import com.phara.pontrix_backend.domain.User;
import com.phara.pontrix_backend.features.admin.dto.UpdateUserRequest;
import com.phara.pontrix_backend.features.admin.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "points", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(UpdateUserRequest request, @MappingTarget User user);
}


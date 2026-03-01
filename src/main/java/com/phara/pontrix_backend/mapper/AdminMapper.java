package com.phara.pontrix_backend.mapper;

import com.phara.pontrix_backend.domain.Admin;
import com.phara.pontrix_backend.features.admin.dto.AdminLoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    AdminLoginResponse toLoginResponse(Admin admin,
                                       String accessToken,
                                       String refreshToken);
}
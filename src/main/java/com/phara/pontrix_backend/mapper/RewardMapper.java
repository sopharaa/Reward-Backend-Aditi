package com.phara.pontrix_backend.mapper;

import com.phara.pontrix_backend.domain.Reward;
import com.phara.pontrix_backend.features.rewards.dto.CreateRewardRequest;
import com.phara.pontrix_backend.features.rewards.dto.RewardResponse;
import com.phara.pontrix_backend.features.rewards.dto.UpdateRewardRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RewardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Reward toEntity(CreateRewardRequest request);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    RewardResponse toResponse(Reward reward);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(UpdateRewardRequest request, @MappingTarget Reward reward);
}


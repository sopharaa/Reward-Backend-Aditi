package com.phara.pontrix_backend.features.rewards;

import com.phara.pontrix_backend.features.rewards.dto.CreateRewardRequest;
import com.phara.pontrix_backend.features.rewards.dto.RewardResponse;
import com.phara.pontrix_backend.features.rewards.dto.UpdateRewardRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RewardService {

    RewardResponse createReward(CreateRewardRequest request, MultipartFile image);

    RewardResponse updateReward(Long id, UpdateRewardRequest request, MultipartFile image);

    RewardResponse viewReward(Long id);

    List<RewardResponse> viewAllRewards();

    List<RewardResponse> viewRewardsByCompany(Long companyId);

    void deleteReward(Long id);
}


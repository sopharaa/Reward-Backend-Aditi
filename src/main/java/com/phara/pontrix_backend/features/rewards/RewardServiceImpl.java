package com.phara.pontrix_backend.features.rewards;

import com.phara.pontrix_backend.domain.Company;
import com.phara.pontrix_backend.domain.Reward;
import com.phara.pontrix_backend.features.companies.CompanyRepository;
import com.phara.pontrix_backend.features.rewards.dto.CreateRewardRequest;
import com.phara.pontrix_backend.features.rewards.dto.RewardResponse;
import com.phara.pontrix_backend.features.rewards.dto.UpdateRewardRequest;
import com.phara.pontrix_backend.mapper.RewardMapper;
import com.phara.pontrix_backend.service.CloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository;
    private final CompanyRepository companyRepository;
    private final RewardMapper rewardMapper;
    private final CloudStorageService cloudStorageService;

    @Override
    @Transactional
    public RewardResponse createReward(CreateRewardRequest request, MultipartFile image) {
        // Validate company exists
        Company company = companyRepository.findByIdAndDeletedAtIsNull(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Create reward entity
        Reward reward = rewardMapper.toEntity(request);
        reward.setCompany(company);

        // Upload image if provided
        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudStorageService.uploadFile(image, "rewards");
            reward.setImage(imageUrl);
        }

        // Save reward
        Reward savedReward = rewardRepository.save(reward);
        return rewardMapper.toResponse(savedReward);
    }

    @Override
    @Transactional
    public RewardResponse updateReward(Long id, UpdateRewardRequest request, MultipartFile image) {
        // Find existing reward
        Reward reward = rewardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Reward not found"));

        // Validate company exists
        Company company = companyRepository.findByIdAndDeletedAtIsNull(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Store old image URL for deletion
        String oldImageUrl = reward.getImage();

        // Update reward fields
        rewardMapper.updateEntity(request, reward);
        reward.setCompany(company);

        // Handle image update
        if (image != null && !image.isEmpty()) {
            // Delete old image if exists
            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                cloudStorageService.deleteFile(oldImageUrl);
            }

            // Upload new image
            String imageUrl = cloudStorageService.uploadFile(image, "rewards");
            reward.setImage(imageUrl);
        }

        // Save updated reward
        Reward updatedReward = rewardRepository.save(reward);
        return rewardMapper.toResponse(updatedReward);
    }

    @Override
    public RewardResponse viewReward(Long id) {
        Reward reward = rewardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Reward not found"));
        return rewardMapper.toResponse(reward);
    }

    @Override
    public List<RewardResponse> viewAllRewards() {
        return rewardRepository.findByDeletedAtIsNull()
                .stream()
                .map(rewardMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RewardResponse> viewRewardsByCompany(Long companyId) {
        return rewardRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
                .stream()
                .map(rewardMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteReward(Long id) {
        Reward reward = rewardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Reward not found"));

        // Delete image from cloud storage
        if (reward.getImage() != null && !reward.getImage().isEmpty()) {
            cloudStorageService.deleteFile(reward.getImage());
        }

        // Soft delete
        reward.setDeletedAt(LocalDateTime.now());
        rewardRepository.save(reward);
    }
}


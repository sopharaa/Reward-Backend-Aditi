package com.phara.pontrix_backend.features.redemptions;

import com.phara.pontrix_backend.domain.Redeem;
import com.phara.pontrix_backend.domain.Reward;
import com.phara.pontrix_backend.domain.Staff;
import com.phara.pontrix_backend.domain.User;
import com.phara.pontrix_backend.features.redemptions.dto.CreateRedeemRequest;
import com.phara.pontrix_backend.features.redemptions.dto.RedeemResponse;
import com.phara.pontrix_backend.features.redemptions.dto.UpdateRedeemStatusRequest;
import com.phara.pontrix_backend.features.rewards.RewardRepository;
import com.phara.pontrix_backend.features.staff.StaffRepository;
import com.phara.pontrix_backend.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedeemServiceImpl implements RedeemService {

    private final RedeemRepository redeemRepository;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final RewardRepository rewardRepository;

    @Override
    @Transactional
    public RedeemResponse redeemReward(String userEmail, CreateRedeemRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reward reward = rewardRepository.findByIdAndDeletedAtIsNull(request.rewardId())
                .orElseThrow(() -> new RuntimeException("Reward not found"));

        // Validate same company
        if (user.getCompany() == null || reward.getCompany() == null ||
                !user.getCompany().getId().equals(reward.getCompany().getId())) {
            throw new RuntimeException("Reward does not belong to your company");
        }

        // Validate enough points
        long userPoints = user.getPoints() != null ? user.getPoints() : 0L;
        if (userPoints < reward.getPointRequired()) {
            throw new RuntimeException("Insufficient points. You need " + reward.getPointRequired() + " pts but have " + userPoints + " pts.");
        }

        // Validate stock
        if (reward.getStock() <= 0) {
            throw new RuntimeException("This reward is out of stock.");
        }

        // Deduct points from user
        user.setPoints(userPoints - reward.getPointRequired());
        userRepository.save(user);

        // Deduct stock from reward
        reward.setStock(reward.getStock() - 1);
        rewardRepository.save(reward);

        // Create redemption record
        Redeem redeem = new Redeem();
        redeem.setUser(user);
        redeem.setReward(reward);
        redeem.setPointSpend(reward.getPointRequired());
        redeem.setStatus("PENDING");
        redeem.setStaff(null);

        Redeem saved = redeemRepository.save(redeem);
        return toResponse(saved);
    }

    @Override
    public List<RedeemResponse> getMyRedemptions(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return redeemRepository.findByUserIdAndDeletedAtIsNull(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RedeemResponse> getPendingRedemptions(String staffEmail) {
        Staff staff = staffRepository.findByEmail(staffEmail)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        Long companyId = staff.getCompany() != null ? staff.getCompany().getId() : null;
        if (companyId == null) {
            throw new RuntimeException("Staff has no company assigned");
        }

        return redeemRepository.findByStatusAndCompanyIdAndDeletedAtIsNull("PENDING", companyId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RedeemResponse> getCompanyRedemptions(String staffEmail) {
        Staff staff = staffRepository.findByEmail(staffEmail)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        Long companyId = staff.getCompany() != null ? staff.getCompany().getId() : null;
        if (companyId == null) return List.of();

        return redeemRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RedeemResponse updateRedeemStatus(String staffEmail, Long redeemId, UpdateRedeemStatusRequest request) {
        Staff staff = staffRepository.findByEmail(staffEmail)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        Redeem redeem = redeemRepository.findByIdAndDeletedAtIsNull(redeemId)
                .orElseThrow(() -> new RuntimeException("Redemption request not found"));

        if (!"PENDING".equals(redeem.getStatus())) {
            throw new RuntimeException("This redemption request has already been processed.");
        }

        // Validate staff belongs to same company as the redeeming user
        if (staff.getCompany() == null || redeem.getUser().getCompany() == null ||
                !staff.getCompany().getId().equals(redeem.getUser().getCompany().getId())) {
            throw new RuntimeException("You are not authorized to review this redemption.");
        }

        String newStatus = request.status().toUpperCase();
        if (!"ACCEPT".equals(newStatus) && !"REJECT".equals(newStatus)) {
            throw new RuntimeException("Invalid status. Must be ACCEPT or REJECT.");
        }

        redeem.setStaff(staff);
        redeem.setStatus(newStatus);

        if ("REJECT".equals(newStatus)) {
            // Return points back to user
            User user = redeem.getUser();
            long currentPoints = user.getPoints() != null ? user.getPoints() : 0L;
            user.setPoints(currentPoints + redeem.getPointSpend());
            userRepository.save(user);

            // Return stock back to reward
            Reward reward = redeem.getReward();
            reward.setStock(reward.getStock() + 1);
            rewardRepository.save(reward);
        }
        // ACCEPT: points and stock already deducted when user submitted, no changes needed

        Redeem saved = redeemRepository.save(redeem);
        return toResponse(saved);
    }

    private RedeemResponse toResponse(Redeem redeem) {
        Long staffId = redeem.getStaff() != null ? redeem.getStaff().getId() : null;
        String staffName = redeem.getStaff() != null ? redeem.getStaff().getName() : null;
        return new RedeemResponse(
                redeem.getId(),
                redeem.getUser().getId(),
                redeem.getUser().getName(),
                staffId,
                staffName,
                redeem.getReward().getId(),
                redeem.getReward().getName(),
                redeem.getPointSpend(),
                redeem.getStatus(),
                redeem.getCreatedAt(),
                redeem.getUpdatedAt()
        );
    }
}


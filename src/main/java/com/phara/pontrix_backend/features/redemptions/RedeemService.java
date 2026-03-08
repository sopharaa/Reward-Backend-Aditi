package com.phara.pontrix_backend.features.redemptions;

import com.phara.pontrix_backend.features.redemptions.dto.CreateRedeemRequest;
import com.phara.pontrix_backend.features.redemptions.dto.RedeemResponse;
import com.phara.pontrix_backend.features.redemptions.dto.UpdateRedeemStatusRequest;

import java.util.List;

public interface RedeemService {
    RedeemResponse redeemReward(String userEmail, CreateRedeemRequest request);
    List<RedeemResponse> getMyRedemptions(String userEmail);
    List<RedeemResponse> getPendingRedemptions(String staffEmail);
    RedeemResponse updateRedeemStatus(String staffEmail, Long redeemId, UpdateRedeemStatusRequest request);
}


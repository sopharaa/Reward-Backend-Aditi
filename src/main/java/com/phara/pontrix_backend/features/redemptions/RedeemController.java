package com.phara.pontrix_backend.features.redemptions;

import com.phara.pontrix_backend.features.redemptions.dto.RedeemResponse;
import com.phara.pontrix_backend.features.redemptions.dto.UpdateRedeemStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class RedeemController {

    private final RedeemService redeemService;

    // ──── STAFF endpoints (/api/staff/**) ────────────────────────────────────────

    @GetMapping("/api/staff/redemptions")
    public ResponseEntity<List<RedeemResponse>> getPendingRedemptions(Principal principal) {
        List<RedeemResponse> response = redeemService.getPendingRedemptions(principal.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/staff/redemptions/{id}")
    public ResponseEntity<?> updateRedeemStatus(
            @PathVariable Long id,
            @RequestBody UpdateRedeemStatusRequest request,
            Principal principal) {
        try {
            RedeemResponse response = redeemService.updateRedeemStatus(principal.getName(), id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    record ErrorResponse(String message) {}
}



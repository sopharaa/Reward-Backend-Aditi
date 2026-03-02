package com.phara.pontrix_backend.features.staff;

import com.phara.pontrix_backend.features.staff.dto.StaffLoginRequest;
import com.phara.pontrix_backend.features.staff.dto.StaffLoginResponse;
import com.phara.pontrix_backend.features.staff.dto.StaffProfileResponse;
import com.phara.pontrix_backend.features.staff.dto.UpdateStaffProfileRequest;
import org.springframework.web.multipart.MultipartFile;

public interface StaffService {
    StaffLoginResponse login(StaffLoginRequest request);
    void logout(String token);
    StaffProfileResponse getProfile(String email);
    StaffProfileResponse updateProfile(String email, UpdateStaffProfileRequest request, MultipartFile profileImage);
}


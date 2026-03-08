package com.phara.pontrix_backend.features.staff;

import com.phara.pontrix_backend.features.staff.dto.StaffLoginRequest;
import com.phara.pontrix_backend.features.staff.dto.StaffLoginResponse;
import com.phara.pontrix_backend.features.staff.dto.StaffProfileResponse;
import com.phara.pontrix_backend.features.staff.dto.UpdateStaffProfileRequest;
import com.phara.pontrix_backend.features.user.dto.UserProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StaffService {
    StaffLoginResponse login(StaffLoginRequest request);
    void logout(String token);
    StaffProfileResponse getProfile(String email);
    StaffProfileResponse updateProfile(String email, UpdateStaffProfileRequest request, MultipartFile profileImage);
    List<UserProfileResponse> getCompanyUsers(String staffEmail);
}

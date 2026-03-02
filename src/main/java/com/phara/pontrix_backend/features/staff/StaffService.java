package com.phara.pontrix_backend.features.staff;

import com.phara.pontrix_backend.features.staff.dto.StaffLoginRequest;
import com.phara.pontrix_backend.features.staff.dto.StaffLoginResponse;

public interface StaffService {
    StaffLoginResponse login(StaffLoginRequest request);
}


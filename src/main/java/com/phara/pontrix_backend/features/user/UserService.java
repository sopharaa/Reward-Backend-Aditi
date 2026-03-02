package com.phara.pontrix_backend.features.user;
import com.phara.pontrix_backend.features.user.dto.UserLoginRequest;
import com.phara.pontrix_backend.features.user.dto.UserLoginResponse;
import com.phara.pontrix_backend.features.user.dto.UserProfileResponse;
import com.phara.pontrix_backend.features.user.dto.UpdateUserProfileRequest;
import com.phara.pontrix_backend.features.user.dto.UserRegisterRequest;
import com.phara.pontrix_backend.features.user.dto.UserRegisterResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserRegisterResponse register(UserRegisterRequest request);
    UserLoginResponse login(UserLoginRequest request);
    void logout(String token);
    UserProfileResponse getProfile(String email);
    UserProfileResponse updateProfile(String email, UpdateUserProfileRequest request, MultipartFile profileImage);
}
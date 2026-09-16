package com.newsroom.service;

import com.newsroom.dto.user.UpdateProfileRequest;
import com.newsroom.dto.user.UserProfileResponse;
import com.newsroom.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    User handleGetUserByUserName(String username);

    void updateUserToken(String refreshToken, String username);

    UserProfileResponse getCurrentUserProfile();

    UserProfileResponse updateCurrentUserProfile(UpdateProfileRequest request);

    String uploadAvatar(MultipartFile file);
}

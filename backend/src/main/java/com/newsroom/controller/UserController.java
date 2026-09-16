package com.newsroom.controller;

import com.newsroom.commons.ApiPrefixConstants;
import com.newsroom.dto.ResponseDTO.BaseOutput;
import com.newsroom.dto.user.UpdateProfileRequest;
import com.newsroom.dto.user.UserProfileResponse;
import com.newsroom.enums.ResponseStatus;
import com.newsroom.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping(ApiPrefixConstants.API_MAPPING_PREFIX + "/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping("/profile")
    public ResponseEntity<BaseOutput<UserProfileResponse>> getProfile() {
        UserProfileResponse profile = this.userService.getCurrentUserProfile();
        return ResponseEntity.ok(
                BaseOutput.<UserProfileResponse>builder()
                        .status(ResponseStatus.SUCCESS)
                        .data(profile)
                        .message("Lấy thông tin cá nhân thành công")
                        .build()
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<BaseOutput<UserProfileResponse>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        UserProfileResponse updatedProfile = this.userService.updateCurrentUserProfile(request);
        return ResponseEntity.ok(
                BaseOutput.<UserProfileResponse>builder()
                        .status(ResponseStatus.SUCCESS)
                        .data(updatedProfile)
                        .message("Cập nhật thông tin cá nhân thành công")
                        .build()
        );
    }

    @PostMapping("/avatar")
    public ResponseEntity<BaseOutput<Map<String, String>>> uploadAvatar(
            @RequestParam("file") MultipartFile file
    ) {
        String avatarUrl = this.userService.uploadAvatar(file);
        return ResponseEntity.ok(
                BaseOutput.<Map<String, String>>builder()
                        .status(ResponseStatus.SUCCESS)
                        .data(Map.of("avatarUrl", avatarUrl))
                        .message("Tải lên ảnh đại diện thành công")
                        .build()
        );
    }
}

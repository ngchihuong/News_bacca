package com.newsroom.controller;

import com.newsroom.commons.ApiPrefixConstants;
import com.newsroom.dto.ArticleDTO;
import com.newsroom.dto.ResponseDTO.BaseOutput;
import com.newsroom.dto.user.PublicUserProfileResponse;
import com.newsroom.dto.user.UpdateProfileRequest;
import com.newsroom.dto.user.UserProfileResponse;
import com.newsroom.enums.ResponseStatus;
import com.newsroom.service.IArticlesService;
import com.newsroom.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping(ApiPrefixConstants.API_MAPPING_PREFIX + "/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final IArticlesService articlesService;

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

    @GetMapping("/{id}/public")
    public ResponseEntity<BaseOutput<PublicUserProfileResponse>> getPublicProfile(@PathVariable String id) {
        PublicUserProfileResponse profile = this.userService.getPublicUserProfile(id);
        return ResponseEntity.ok(
                BaseOutput.<PublicUserProfileResponse>builder()
                        .status(ResponseStatus.SUCCESS)
                        .data(profile)
                        .message("Lấy thông tin hồ sơ công khai thành công")
                        .build()
        );
    }

    @GetMapping("/{id}/articles")
    public ResponseEntity<BaseOutput<Page<ArticleDTO>>> getAuthorArticles(
            @PathVariable String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDTO> articles = this.articlesService.getArticlesByAuthor(id, pageable);
        return ResponseEntity.ok(
                BaseOutput.<Page<ArticleDTO>>builder()
                        .status(ResponseStatus.SUCCESS)
                        .data(articles)
                        .message("Lấy danh sách bài viết tác giả thành công")
                        .build()
        );
    }
}

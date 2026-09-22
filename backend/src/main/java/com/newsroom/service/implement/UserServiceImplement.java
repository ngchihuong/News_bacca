package com.newsroom.service.implement;

import com.newsroom.config.exceptions.AppException;
import com.newsroom.dto.user.PublicUserProfileResponse;
import com.newsroom.dto.user.UpdateProfileRequest;
import com.newsroom.dto.user.UserProfileResponse;
import com.newsroom.enums.ErrorCode;
import com.newsroom.model.User;
import com.newsroom.repository.UserRepository;
import com.newsroom.security.SecurityUtil;
import com.newsroom.service.IUserService;
import com.newsroom.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements IUserService {
    private final UserRepository userRepository;
    private final MinioService minioService;

    @Override
    public User handleGetUserByUserName(String identifier) {
        if (identifier == null || identifier.isBlank()) return null;
        String id = identifier.trim();
        User user = this.userRepository.findByEmail(id.toLowerCase());
        if (user != null) return user;
        user = this.userRepository.findFirstByPhone(id);
        if (user != null) return user;
        return this.userRepository.findByUsername(id).orElse(null);
    }

    @Override
    public void updateUserToken(String refreshToken, String identifier) {
        if (identifier == null || identifier.isBlank()) return;
        User currentUser = this.handleGetUserByUserName(identifier);
        if (currentUser != null) {
            currentUser.setRefreshToken(refreshToken);
            this.userRepository.save(currentUser);
        }
    }

    private User getCurrentAuthenticatedUser() {
        String identifier = SecurityUtil.getCurrentUserLogin().orElse(null);
        if (identifier == null || identifier.isBlank()) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Vui lòng đăng nhập để tiếp tục");
        }
        User user = this.handleGetUserByUserName(identifier);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "Không tìm thấy người dùng");
        }
        return user;
    }

    private UserProfileResponse mapToProfileResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .bio(user.getBio())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public UserProfileResponse getCurrentUserProfile() {
        User user = getCurrentAuthenticatedUser();
        return mapToProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateCurrentUserProfile(UpdateProfileRequest request) {
        User user = getCurrentAuthenticatedUser();
        if (request != null) {
            if (request.getBio() != null && request.getBio().length() > 500) {
                throw new AppException(ErrorCode.BIO_TOO_LONG);
            }
            if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
                user.setFullName(request.getFullName().trim());
            }
            if (request.getPhone() != null) {
                user.setPhone(request.getPhone().trim());
            }
            if (request.getBio() != null) {
                user.setBio(request.getBio().trim());
            }
            user.setUpdatedAt(Instant.now());
            user = this.userRepository.save(user);
        }
        return mapToProfileResponse(user);
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        User user = getCurrentAuthenticatedUser();
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.FIELD_REQUIRED, "Tệp tải lên không được để trống");
        }
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equalsIgnoreCase("image/jpeg")
                && !contentType.equalsIgnoreCase("image/png")
                && !contentType.equalsIgnoreCase("image/webp"))) {
            throw new AppException(ErrorCode.FILE_INVALID_TYPE);
        }

        String fileName = this.minioService.uploadToMinio(file);
        String avatarUrl = "/public/view/image/" + fileName;
        user.setAvatarUrl(avatarUrl);
        user.setUpdatedAt(Instant.now());
        this.userRepository.save(user);

        return avatarUrl;
    }

    @Override
    public PublicUserProfileResponse getPublicUserProfile(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = this.userRepository.findById(userId.trim())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!user.isActive()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "Hồ sơ người dùng không khả dụng hoặc đã bị vô hiệu hóa");
        }

        return PublicUserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName() != null && !user.getFullName().isBlank() ? user.getFullName() : user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .isJournalistVerified(user.isJournalistVerified())
                .journalistOrganization(user.getJournalistOrganization())
                .followersCount(user.getFollowersCount())
                .followingCount(user.getFollowingCount())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

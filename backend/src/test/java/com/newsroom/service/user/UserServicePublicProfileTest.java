package com.newsroom.service.user;

import com.newsroom.config.exceptions.AppException;
import com.newsroom.dto.user.PublicUserProfileResponse;
import com.newsroom.enums.ErrorCode;
import com.newsroom.model.User;
import com.newsroom.repository.UserRepository;
import com.newsroom.service.MinioService;
import com.newsroom.service.implement.UserServiceImplement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServicePublicProfileTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MinioService minioService;

    @InjectMocks
    private UserServiceImplement userService;

    @Test
    @DisplayName("Thành công: Lấy hồ sơ công khai của người dùng thông thường")
    void getPublicUserProfile_standardUser_success() {
        String userId = "user101";
        User mockUser = User.builder()
                .id(userId)
                .username("john_doe")
                .fullName("John Doe")
                .email("john@example.com")
                .phone("0912345678")
                .password("secretHash")
                .bio("Lập trình viên và độc giả tin tức")
                .avatarUrl("/public/view/image/john.jpg")
                .role("ROLE_USER")
                .active(true)
                .isJournalistVerified(false)
                .followersCount(15)
                .followingCount(8)
                .createdAt(Instant.parse("2026-01-10T10:00:00Z"))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        PublicUserProfileResponse response = userService.getPublicUserProfile(userId);

        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals("john_doe", response.getUsername());
        assertEquals("John Doe", response.getFullName());
        assertEquals("Lập trình viên và độc giả tin tức", response.getBio());
        assertEquals("/public/view/image/john.jpg", response.getAvatarUrl());
        assertFalse(response.isJournalistVerified());
        assertNull(response.getJournalistOrganization());
        assertEquals(15, response.getFollowersCount());
        assertEquals(8, response.getFollowingCount());
        assertEquals(Instant.parse("2026-01-10T10:00:00Z"), response.getCreatedAt());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Thành công: Lấy hồ sơ công khai của Nhà báo xác thực")
    void getPublicUserProfile_journalistUser_success() {
        String userId = "journalist202";
        User journalistUser = User.builder()
                .id(userId)
                .username("journalist_jane")
                .fullName("Jane Reporter")
                .email("jane@press.com")
                .bio("Nhà báo điều tra xã hội")
                .avatarUrl("/public/view/image/jane.jpg")
                .role("ROLE_JOURNALIST")
                .active(true)
                .isJournalistVerified(true)
                .journalistOrganization("Báo Tuổi Trẻ")
                .followersCount(1250)
                .followingCount(40)
                .createdAt(Instant.parse("2025-06-01T08:00:00Z"))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(journalistUser));

        PublicUserProfileResponse response = userService.getPublicUserProfile(userId);

        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals("journalist_jane", response.getUsername());
        assertEquals("Jane Reporter", response.getFullName());
        assertTrue(response.isJournalistVerified());
        assertEquals("Báo Tuổi Trẻ", response.getJournalistOrganization());
        assertEquals(1250, response.getFollowersCount());
        assertEquals(40, response.getFollowingCount());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Thất bại: ID người dùng không tồn tại trong database")
    void getPublicUserProfile_notFound_throwsException() {
        String nonExistentId = "non_existent_id";
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () ->
                userService.getPublicUserProfile(nonExistentId)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(userRepository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("Thất bại: ID người dùng bị null hoặc khoảng trắng")
    void getPublicUserProfile_blankId_throwsException() {
        AppException ex1 = assertThrows(AppException.class, () -> userService.getPublicUserProfile(null));
        assertEquals(ErrorCode.USER_NOT_FOUND, ex1.getErrorCode());

        AppException ex2 = assertThrows(AppException.class, () -> userService.getPublicUserProfile("   "));
        assertEquals(ErrorCode.USER_NOT_FOUND, ex2.getErrorCode());

        verify(userRepository, never()).findById(anyString());
    }

    @Test
    @DisplayName("Thất bại: Tài khoản người dùng bị khóa hoặc chưa active")
    void getPublicUserProfile_inactiveUser_throwsException() {
        String inactiveUserId = "inactiveUser1";
        User inactiveUser = User.builder()
                .id(inactiveUserId)
                .username("banned_user")
                .fullName("Banned User")
                .active(false)
                .build();

        when(userRepository.findById(inactiveUserId)).thenReturn(Optional.of(inactiveUser));

        AppException exception = assertThrows(AppException.class, () ->
                userService.getPublicUserProfile(inactiveUserId)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(userRepository, times(1)).findById(inactiveUserId);
    }
}

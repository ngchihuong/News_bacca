package com.newsroom.service.user;

import com.newsroom.config.exceptions.AppException;
import com.newsroom.dto.user.UpdateProfileRequest;
import com.newsroom.dto.user.UserProfileResponse;
import com.newsroom.enums.ErrorCode;
import com.newsroom.model.User;
import com.newsroom.repository.UserRepository;
import com.newsroom.service.MinioService;
import com.newsroom.service.implement.UserServiceImplement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceProfileTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MinioService minioService;

    @InjectMocks
    private UserServiceImplement userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id("user123")
                .username("testuser")
                .email("testuser@newsroom.com")
                .fullName("Test User")
                .phone("0987654321")
                .bio("Lập trình viên backend")
                .avatarUrl("/public/view/image/default.jpg")
                .role("ROLE_USER")
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("testuser@newsroom.com", "password");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Lấy thông tin cá nhân thành công")
    void testGetCurrentUserProfile_Success() {
        when(userRepository.findByEmail("testuser@newsroom.com")).thenReturn(mockUser);

        UserProfileResponse response = userService.getCurrentUserProfile();

        assertNotNull(response);
        assertEquals("user123", response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("testuser@newsroom.com", response.getEmail());
        assertEquals("Test User", response.getFullName());
        assertEquals("0987654321", response.getPhone());
        assertEquals("Lập trình viên backend", response.getBio());
    }

    @Test
    @DisplayName("Cập nhật thông tin cá nhân thành công")
    void testUpdateCurrentUserProfile_Success() {
        when(userRepository.findByEmail("testuser@newsroom.com")).thenReturn(mockUser);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .fullName("Test User New")
                .phone("0912345678")
                .bio("Tiểu sử mới đã được cập nhật")
                .build();

        UserProfileResponse response = userService.updateCurrentUserProfile(request);

        assertNotNull(response);
        assertEquals("Test User New", response.getFullName());
        assertEquals("0912345678", response.getPhone());
        assertEquals("Tiểu sử mới đã được cập nhật", response.getBio());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    @DisplayName("Cập nhật bio quá 500 ký tự -> Ném BIO_TOO_LONG")
    void testUpdateCurrentUserProfile_BioTooLong() {
        when(userRepository.findByEmail("testuser@newsroom.com")).thenReturn(mockUser);

        String longBio = "A".repeat(501);
        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .bio(longBio)
                .build();

        AppException exception = assertThrows(AppException.class, () ->
                userService.updateCurrentUserProfile(request)
        );

        assertEquals(ErrorCode.BIO_TOO_LONG, exception.getErrorCode());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Upload avatar hợp lệ thành công")
    void testUploadAvatar_Success() {
        when(userRepository.findByEmail("testuser@newsroom.com")).thenReturn(mockUser);
        when(minioService.uploadToMinio(any())).thenReturn("avatar_123.jpg");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3, 4}
        );

        String avatarUrl = userService.uploadAvatar(file);

        assertNotNull(avatarUrl);
        assertEquals("/public/view/image/avatar_123.jpg", avatarUrl);
        assertEquals("/public/view/image/avatar_123.jpg", mockUser.getAvatarUrl());
        verify(minioService, times(1)).uploadToMinio(file);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    @DisplayName("Upload avatar file rỗng -> Ném FIELD_REQUIRED")
    void testUploadAvatar_EmptyFile() {
        when(userRepository.findByEmail("testuser@newsroom.com")).thenReturn(mockUser);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.jpg",
                "image/jpeg",
                new byte[]{}
        );

        AppException exception = assertThrows(AppException.class, () ->
                userService.uploadAvatar(file)
        );

        assertEquals(ErrorCode.FIELD_REQUIRED, exception.getErrorCode());
        verify(minioService, never()).uploadToMinio(any());
    }

    @Test
    @DisplayName("Upload avatar vượt quá 2MB -> Ném FILE_TOO_LARGE")
    void testUploadAvatar_FileTooLarge() {
        when(userRepository.findByEmail("testuser@newsroom.com")).thenReturn(mockUser);

        byte[] largeBytes = new byte[2 * 1024 * 1024 + 1];
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.jpg",
                "image/jpeg",
                largeBytes
        );

        AppException exception = assertThrows(AppException.class, () ->
                userService.uploadAvatar(file)
        );

        assertEquals(ErrorCode.FILE_TOO_LARGE, exception.getErrorCode());
        verify(minioService, never()).uploadToMinio(any());
    }

    @Test
    @DisplayName("Upload avatar sai định dạng mime (text/plain) -> Ném FILE_INVALID_TYPE")
    void testUploadAvatar_InvalidType() {
        when(userRepository.findByEmail("testuser@newsroom.com")).thenReturn(mockUser);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "malicious.txt",
                "text/plain",
                "Hello World".getBytes()
        );

        AppException exception = assertThrows(AppException.class, () ->
                userService.uploadAvatar(file)
        );

        assertEquals(ErrorCode.FILE_INVALID_TYPE, exception.getErrorCode());
        verify(minioService, never()).uploadToMinio(any());
    }
}

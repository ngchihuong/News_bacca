package com.newsroom.service.auth;

import com.newsroom.config.exceptions.AppException;
import com.newsroom.dto.auth.JwtResponse;
import com.newsroom.dto.auth.LoginRequest;
import com.newsroom.enums.ErrorCode;
import com.newsroom.model.User;
import com.newsroom.repository.UserRepository;
import com.newsroom.security.SecurityUtil;
import com.newsroom.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceLoginTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private IUserService userService;

    @Mock
    private SecurityUtil securityUtil;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(
                userRepository,
                passwordEncoder,
                authenticationManagerBuilder,
                userService,
                securityUtil
        );
    }

    @Test
    @DisplayName("Đăng nhập thành công bằng Email (Happy Path)")
    void testLoginWithEmailSuccess() {
        LoginRequest request = new LoginRequest();
        request.setUsername("reader@newsroom.vn");
        request.setPassword("correctPassword");

        User user = new User();
        user.setId("u-123");
        user.setEmail("reader@newsroom.vn");
        user.setUsername("reader");
        user.setPassword("$2a$10$encodedPassword");
        user.setActive(true);
        user.setRole("ROLE_USER");
        user.setFailedLoginAttempts(2);

        when(userRepository.findByEmail("reader@newsroom.vn")).thenReturn(user);
        when(passwordEncoder.matches("correctPassword", "$2a$10$encodedPassword")).thenReturn(true);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(securityUtil.createAccessToken(anyString(), any())).thenReturn("mock-access-token");
        when(securityUtil.createRefreshToken(anyString(), any())).thenReturn("mock-refresh-token");

        JwtResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mock-access-token", response.getAccessToken());
        assertEquals("reader", response.getUser().getName());
        assertEquals("ROLE_USER", response.getUser().getRole());
        assertEquals(0, user.getFailedLoginAttempts());
        assertNull(user.getLockoutUntil());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Đăng nhập thành công bằng Số điện thoại (SĐT)")
    void testLoginWithPhoneSuccess() {
        LoginRequest request = new LoginRequest();
        request.setUsername("0912345678");
        request.setPassword("correctPassword");

        User user = new User();
        user.setId("u-phone-123");
        user.setPhone("0912345678");
        user.setUsername("phoneuser");
        user.setPassword("$2a$10$encodedPassword");
        user.setActive(true);
        user.setRole("ROLE_USER");

        when(userRepository.findByPhone("0912345678")).thenReturn(user);
        when(passwordEncoder.matches("correctPassword", "$2a$10$encodedPassword")).thenReturn(true);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(securityUtil.createAccessToken(anyString(), any())).thenReturn("mock-access-token");
        when(securityUtil.createRefreshToken(anyString(), any())).thenReturn("mock-refresh-token");

        JwtResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mock-access-token", response.getAccessToken());
        assertEquals("phoneuser", response.getUser().getName());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Đăng nhập thất bại khi tài khoản không tồn tại (INVALID_CREDENTIALS để chống user enumeration)")
    void testLoginUserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("unknown@newsroom.vn");
        request.setPassword("password");

        when(userRepository.findByEmail("unknown@newsroom.vn")).thenReturn(null);

        AppException ex = assertThrows(AppException.class, () -> authService.login(request));
        assertEquals(ErrorCode.INVALID_CREDENTIALS, ex.getErrorCode());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Sai mật khẩu lần 1: Đếm số lần và báo số lần thử còn lại (INVALID_CREDENTIALS)")
    void testLoginWrongPasswordAttempt1() {
        LoginRequest request = new LoginRequest();
        request.setUsername("reader@newsroom.vn");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setId("u-123");
        user.setEmail("reader@newsroom.vn");
        user.setPassword("$2a$10$encodedPassword");
        user.setActive(true);
        user.setFailedLoginAttempts(0);

        when(userRepository.findByEmail("reader@newsroom.vn")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "$2a$10$encodedPassword")).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> authService.login(request));
        assertEquals(ErrorCode.INVALID_CREDENTIALS, ex.getErrorCode());
        assertTrue(ex.getMessage().contains("4 lần thử"));
        assertEquals(1, user.getFailedLoginAttempts());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Sai mật khẩu lần thứ 5: Khóa tài khoản 15 phút (ACCOUNT_LOCKED)")
    void testLoginWrongPasswordAttempt5LocksAccount() {
        LoginRequest request = new LoginRequest();
        request.setUsername("reader@newsroom.vn");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setId("u-123");
        user.setEmail("reader@newsroom.vn");
        user.setPassword("$2a$10$encodedPassword");
        user.setActive(true);
        user.setFailedLoginAttempts(4);

        when(userRepository.findByEmail("reader@newsroom.vn")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "$2a$10$encodedPassword")).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> authService.login(request));
        assertEquals(ErrorCode.ACCOUNT_LOCKED, ex.getErrorCode());
        assertTrue(ex.getMessage().contains("tạm khóa 15 phút"));
        assertEquals(5, user.getFailedLoginAttempts());
        assertNotNull(user.getLockoutUntil());
        assertTrue(user.getLockoutUntil().isAfter(Instant.now()));
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Cố gắng đăng nhập khi tài khoản đang trong thời gian bị khóa")
    void testLoginAccountCurrentlyLocked() {
        LoginRequest request = new LoginRequest();
        request.setUsername("reader@newsroom.vn");
        request.setPassword("anyPassword");

        User user = new User();
        user.setId("u-123");
        user.setEmail("reader@newsroom.vn");
        user.setActive(true);
        user.setLockoutUntil(Instant.now().plus(10, ChronoUnit.MINUTES));

        when(userRepository.findByEmail("reader@newsroom.vn")).thenReturn(user);

        AppException ex = assertThrows(AppException.class, () -> authService.login(request));
        assertEquals(ErrorCode.ACCOUNT_LOCKED, ex.getErrorCode());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Đăng nhập thất bại khi tài khoản bị vô hiệu hóa (ACCOUNT_DISABLED)")
    void testLoginDisabledAccountThrowsAccountDisabled() {
        LoginRequest request = new LoginRequest();
        request.setUsername("disabled@newsroom.vn");
        request.setPassword("anyPassword");

        User user = new User();
        user.setId("u-disabled");
        user.setEmail("disabled@newsroom.vn");
        user.setActive(false);

        when(userRepository.findByEmail("disabled@newsroom.vn")).thenReturn(user);

        AppException ex = assertThrows(AppException.class, () -> authService.login(request));
        assertEquals(ErrorCode.ACCOUNT_DISABLED, ex.getErrorCode());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Đăng nhập sau khi hết thời gian khóa: Mở khóa và đăng nhập thành công")
    void testLoginAccountLockoutExpiredAllowsLogin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("reader@newsroom.vn");
        request.setPassword("correctPassword");

        User user = new User();
        user.setId("u-123");
        user.setEmail("reader@newsroom.vn");
        user.setUsername("reader");
        user.setPassword("$2a$10$encodedPassword");
        user.setActive(true);
        user.setFailedLoginAttempts(5);
        // Lockout expired 2 minutes ago
        user.setLockoutUntil(Instant.now().minus(2, ChronoUnit.MINUTES));

        when(userRepository.findByEmail("reader@newsroom.vn")).thenReturn(user);
        when(passwordEncoder.matches("correctPassword", "$2a$10$encodedPassword")).thenReturn(true);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(securityUtil.createAccessToken(anyString(), any())).thenReturn("mock-access-token");
        when(securityUtil.createRefreshToken(anyString(), any())).thenReturn("mock-refresh-token");

        JwtResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals(0, user.getFailedLoginAttempts());
        assertNull(user.getLockoutUntil());
        assertTrue(user.isActive());
        verify(userRepository, atLeastOnce()).save(user);
    }

    @Test
    @DisplayName("Nhập sai mật khẩu sau khi hết hạn khóa: Bắt đầu chu kỳ 5 lần thử mới thay vì bị khóa ngay")
    void testLoginWrongPasswordAfterLockoutExpiredStartsFreshAttempts() {
        LoginRequest request = new LoginRequest();
        request.setUsername("reader@newsroom.vn");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setId("u-123");
        user.setEmail("reader@newsroom.vn");
        user.setPassword("$2a$10$encodedPassword");
        user.setActive(true);
        user.setFailedLoginAttempts(5);
        // Lockout expired 2 minutes ago
        user.setLockoutUntil(Instant.now().minus(2, ChronoUnit.MINUTES));

        when(userRepository.findByEmail("reader@newsroom.vn")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "$2a$10$encodedPassword")).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> authService.login(request));
        assertEquals(ErrorCode.INVALID_CREDENTIALS, ex.getErrorCode());
        assertTrue(ex.getMessage().contains("4 lần thử"));
        assertEquals(1, user.getFailedLoginAttempts());
        assertNull(user.getLockoutUntil());
        verify(userRepository, atLeastOnce()).save(user);
    }
}

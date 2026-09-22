package com.newsroom.service.auth;

import com.newsroom.config.exceptions.NewsCommonException;
import com.newsroom.dto.RegisterRequest;
import com.newsroom.dto.UserDTO;
import com.newsroom.model.User;
import com.newsroom.repository.UserRepository;
import com.newsroom.security.SecurityUtil;
import com.newsroom.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceRegisterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

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
    @DisplayName("Đăng ký thành công với thông tin hợp lệ (Happy Path)")
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@newsroom.vn");
        request.setPassword("password123");
        request.setFullName("Nguyen Van A");
        request.setUsername("nguyenvana");

        when(userRepository.existsByEmail("test@newsroom.vn")).thenReturn(false);
        when(userRepository.existsByUsername("nguyenvana")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$encodedPasswordHash");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId("mock-user-id-123");
            return saved;
        });

        UserDTO result = authService.register(request);

        assertNotNull(result);
        assertEquals("mock-user-id-123", result.getId());
        assertEquals("test@newsroom.vn", result.getEmail());
        assertEquals("Nguyen Van A", result.getFullName());
        assertEquals("nguyenvana", result.getUsername());
        assertEquals("ROLE_USER", result.getRoles());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("ROLE_USER", savedUser.getRole());
        assertTrue(savedUser.isActive());
        assertEquals("$2a$10$encodedPasswordHash", savedUser.getPassword());
        assertNotNull(savedUser.getCreatedAt());
    }

    @Test
    @DisplayName("Đăng ký thất bại khi email đã tồn tại")
    void testRegisterDuplicateEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("duplicate@newsroom.vn");
        request.setPassword("password123");
        request.setFullName("Duplicate User");

        when(userRepository.existsByEmail("duplicate@newsroom.vn")).thenReturn(true);

        NewsCommonException exception = assertThrows(
                NewsCommonException.class,
                () -> authService.register(request)
        );

        assertEquals("Email đã được sử dụng", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Tự động sinh username từ email prefix khi không nhập username")
    void testRegisterAutoGenerateUsername() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("editor.news@newsroom.vn");
        request.setPassword("password123");
        request.setFullName("Editor News");
        request.setUsername(null);

        when(userRepository.existsByEmail("editor.news@newsroom.vn")).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedHash");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId("user-id-456");
            return saved;
        });

        UserDTO result = authService.register(request);

        assertNotNull(result);
        assertNotNull(result.getUsername());
        assertFalse(result.getUsername().isBlank());
        assertTrue(result.getUsername().startsWith("editor"));

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Tự động tăng suffix khi username bị trùng lặp")
    void testRegisterResolveDuplicateUsername() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("dupeuser@newsroom.vn");
        request.setPassword("password123");
        request.setFullName("Dupe User");
        request.setUsername("dupeuser");

        when(userRepository.existsByEmail("dupeuser@newsroom.vn")).thenReturn(false);
        // First existsByUsername returns true, second returns false
        when(userRepository.existsByUsername("dupeuser")).thenReturn(true);
        when(userRepository.existsByUsername("dupeuser1")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedHash");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO result = authService.register(request);

        assertNotNull(result);
        assertEquals("dupeuser1", result.getUsername());
        verify(userRepository).save(any(User.class));
    }
}

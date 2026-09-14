package com.newsroom.service.auth;

import com.newsroom.commons.Constants;
import com.newsroom.config.exceptions.AppException;
import com.newsroom.config.exceptions.NewsCommonException;
import com.newsroom.dto.UserDTO;
import com.newsroom.dto.auth.JwtResponse;
import com.newsroom.dto.auth.LoginRequest;
import com.newsroom.enums.ErrorCode;
import com.newsroom.model.User;
import com.newsroom.repository.UserRepository;
import com.newsroom.security.SecurityUtil;
import com.newsroom.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final IUserService userService;
    private final SecurityUtil securityUtil;

    @Value("${app.jwt.expiration-refresh-token}")
    private long expirationRefreshToken;

    public User findUserByIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) return null;
        String id = identifier.trim();
        User user = this.userRepository.findByEmail(id.toLowerCase());
        if (user != null) return user;
        user = this.userRepository.findByPhone(id);
        if (user != null) return user;
        return this.userRepository.findByUsername(id).orElse(null);
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getUsername().isBlank()) {
            throw new AppException(ErrorCode.FIELD_REQUIRED, "Tài khoản (Email hoặc Số điện thoại) không được để trống");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new AppException(ErrorCode.FIELD_REQUIRED, "Mật khẩu không được để trống");
        }

        User currentUserDb = this.findUserByIdentifier(request.getUsername());
        if (currentUserDb == null) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        // Kiểm tra tài khoản có đang bị khóa lockout 15 phút do nhập sai 5 lần liên tiếp
        Instant now = Instant.now();
        if (currentUserDb.getLockoutUntil() != null && currentUserDb.getLockoutUntil().isAfter(now)) {
            long minutesRemaining = Duration.between(now, currentUserDb.getLockoutUntil()).toMinutes() + 1;
            throw new AppException(ErrorCode.ACCOUNT_LOCKED,
                    "Tài khoản tạm thời bị khóa do nhập sai mật khẩu quá 5 lần. Vui lòng thử lại sau " + minutesRemaining + " phút.");
        }

        // Kiểm tra tính chính xác của mật khẩu
        boolean matches = this.passwordEncoder.matches(request.getPassword(), currentUserDb.getPassword());
        if (!matches) {
            int attempts = currentUserDb.getFailedLoginAttempts() + 1;
            currentUserDb.setFailedLoginAttempts(attempts);
            if (attempts >= 5) {
                currentUserDb.setLockoutUntil(now.plus(15, ChronoUnit.MINUTES));
                this.userRepository.save(currentUserDb);
                throw new AppException(ErrorCode.ACCOUNT_LOCKED,
                        "Bạn đã nhập sai mật khẩu 5 lần liên tiếp. Tài khoản đã bị tạm khóa 15 phút.");
            } else {
                this.userRepository.save(currentUserDb);
                int remaining = 5 - attempts;
                throw new AppException(ErrorCode.INVALID_CREDENTIALS,
                        "Email/Số điện thoại hoặc mật khẩu không chính xác. Bạn còn " + remaining + " lần thử trước khi tài khoản bị khóa 15 phút.");
            }
        }

        // Đăng nhập thành công: Reset số lần sai và thời gian khóa
        currentUserDb.setFailedLoginAttempts(0);
        currentUserDb.setLockoutUntil(null);
        currentUserDb.setActive(true);
        this.userRepository.save(currentUserDb);

        String authPrincipal = currentUserDb.getEmail() != null ? currentUserDb.getEmail() : currentUserDb.getUsername();
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(authPrincipal, request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtResponse response = new JwtResponse();
        JwtResponse.UserLogin userLogin = JwtResponse.UserLogin.builder()
                .type("Bearer ")
                .id(currentUserDb.getId())
                .name(currentUserDb.getUsername())
                .username(currentUserDb.getUsername())
                .role(currentUserDb.getRole())
                .build();
        response.setUser(userLogin);

        String access_token = this.securityUtil.createAccessToken(authPrincipal, response.getUser());
        response.setAccessToken(access_token);

        String refresh_token = this.securityUtil.createRefreshToken(authPrincipal, response.getUser());
        this.userService.updateUserToken(refresh_token, currentUserDb.getEmail());

        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expirationRefreshToken)
                .build();

        response.setResponseCookie(responseCookie);
        return response;
    }

    @Override
    public UserDTO register(com.newsroom.dto.RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        boolean existUser = this.userRepository.existsByEmail(normalizedEmail);
        if (existUser) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS, "Email đã được sử dụng");
        }

        User newUser = new User();
        String baseUsername = (request.getUsername() != null && !request.getUsername().isBlank())
                ? request.getUsername().trim()
                : normalizedEmail.split("@")[0].replaceAll("[^a-zA-Z0-9_]", "");
        if (baseUsername.isBlank()) {
            baseUsername = "user";
        }
        String username = baseUsername;
        int suffix = 1;
        while (Boolean.TRUE.equals(this.userRepository.existsByUsername(username))) {
            username = baseUsername + suffix;
            suffix++;
        }

        newUser.setUsername(username);
        newUser.setFullName(request.getFullName().trim());
        newUser.setEmail(normalizedEmail);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole("ROLE_USER");
        newUser.setActive(true);
        newUser.setAvatarUrl("");
        newUser.setCreatedAt(Instant.now());
        newUser.setUpdatedAt(Instant.now());
        this.userRepository.save(newUser);

        return this.convertUserToDTO(newUser);
    }

    @Override
    public void logout() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        if (email == null || email.isEmpty()) {
            throw new NewsCommonException(Constants.ERROR.USER.INVALID_CREDENTIAL);
        }
        User currentUserDb = this.userService.handleGetUserByUserName(email);
        if (currentUserDb == null) {
            throw new NewsCommonException(Constants.ERROR.USER.NOT_EXIST);
        }
        currentUserDb.setActive(false);
        this.userRepository.save(currentUserDb);

        this.userService.updateUserToken(null, email);
    }

    @Override
    public JwtResponse getRefreshToken(String refreshToken) {
        Jwt decodeToken = this.securityUtil.checkValidRefreshToken(refreshToken);
        String email = decodeToken.getSubject();
        User currentUserDb = this.getUserByRefreshTokenAndEmail(refreshToken, email);
        if (currentUserDb == null) {
            throw new NewsCommonException(Constants.ERROR.USER.INVALID_CREDENTIAL);
        }

        JwtResponse res = new JwtResponse();
        if (currentUserDb != null) {
            JwtResponse.UserLogin userLogin = JwtResponse.UserLogin.builder()
                    .type("Bearer ")
                    .id(currentUserDb.getId())
                    .name(currentUserDb.getUsername())
                    .role(currentUserDb.getRole())
                    .build();
            res.setUser(userLogin);
        }
        String access_token = this.securityUtil.createAccessToken(email, res.getUser());

        res.setAccessToken(access_token);

        //create refresh_token
        String new_refresh_token = this.securityUtil.createRefreshToken(email, res.getUser());

//update user
        this.userService.updateUserToken(new_refresh_token, email);

        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expirationRefreshToken)
                .build();
        res.setResponseCookie(responseCookie);
        return res;
    }

    @Override
    public JwtResponse.UserLogin getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUserDb = this.userRepository.findByEmail(email);
        JwtResponse.UserLogin.UserLoginBuilder userLogin
                = JwtResponse.UserLogin.builder();

        if (currentUserDb != null) {
            userLogin.type("Bearer ");
            userLogin.id(currentUserDb.getId());
            userLogin.name(currentUserDb.getUsername());
            userLogin.role(currentUserDb.getRole());
        }
        return userLogin.build();
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    private UserDTO convertUserToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(user.getRole())
                .age(user.getAge())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}


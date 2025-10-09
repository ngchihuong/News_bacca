package com.newsroom.service.auth;

import com.newsroom.commons.Constants;
import com.newsroom.config.exceptions.NewsCommonException;
import com.newsroom.dto.UserDTO;
import com.newsroom.dto.auth.JwtResponse;
import com.newsroom.dto.auth.LoginRequest;
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

import java.time.Instant;

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

    @Override
    public JwtResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtResponse response = new JwtResponse();
        User currentUserDb = this.userService.handleGetUserByUserName(request.getUsername());
        if (currentUserDb != null) {
            JwtResponse.UserLogin userLogin
                    = JwtResponse.UserLogin.builder()
                    .type("Bearer ")
                    .id(currentUserDb.getId())
                    .name(currentUserDb.getUsername())
                    .role(currentUserDb.getRole())
                    .build();
            response.setUser(userLogin);
        }
        assert currentUserDb != null;
        currentUserDb.setActive(true);
        this.userRepository.save(currentUserDb);

        String access_token = this.securityUtil.createAccessToken(authentication.getName(), response.getUser());
        response.setAccessToken(access_token);

        String refresh_token = this.securityUtil.createRefreshToken(authentication.getName(), response.getUser());

        this.userService.updateUserToken(refresh_token, request.getUsername());

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
    public UserDTO register(User user) {
        boolean existUser = this.userRepository.existsByEmail(user.getEmail());
        if (existUser) {
            throw new NewsCommonException(Constants.ERROR.USER.EXIST);
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setPhone(user.getPhone() == null ? "" : user.getPhone());
        newUser.setAge(user.getAge() == null ? null : user.getAge());
        newUser.setRole("VIEWER");
        newUser.setActive(true);
        newUser.setAvatarUrl(user.getAvatarUrl() == null ? "" : user.getAvatarUrl());
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

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    private UserDTO convertUserToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(user.getRole())
                .age(user.getAge())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

//
//    @Override
//    public User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("Current user not found"));
//    }
}


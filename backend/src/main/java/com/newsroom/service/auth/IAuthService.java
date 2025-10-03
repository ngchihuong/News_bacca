package com.newsroom.service.auth;

import com.newsroom.dto.AuthRequest;
import com.newsroom.dto.AuthResponse;
import com.newsroom.dto.RegisterRequest;
import com.newsroom.dto.UserDTO;
import com.newsroom.dto.auth.JwtResponse;
import com.newsroom.dto.auth.LoginRequest;
import com.newsroom.model.User;
import jakarta.validation.Valid;

public interface IAuthService {
    JwtResponse login(@Valid LoginRequest request);

    UserDTO register(@Valid User user);

    void logout();
//    AuthResponse login(AuthRequest request);
//    User register(RegisterRequest request);
//    User getCurrentUser();
}


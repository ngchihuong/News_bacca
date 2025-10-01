package com.newsroom.service;

import com.newsroom.dto.AuthRequest;
import com.newsroom.dto.AuthResponse;
import com.newsroom.dto.RegisterRequest;
import com.newsroom.model.User;

public interface IAuthService {
    AuthResponse login(AuthRequest request);
    User register(RegisterRequest request);
    User getCurrentUser();
}


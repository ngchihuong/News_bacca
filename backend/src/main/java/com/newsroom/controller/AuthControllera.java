package com.newsroom.controller;

import com.newsroom.dto.AuthRequest;
import com.newsroom.dto.AuthResponse;
import com.newsroom.dto.RegisterRequest;
import com.newsroom.model.User;
import com.newsroom.service.auth.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthControllera {
    
//    private final IAuthService authService;
//
//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
//        AuthResponse response = authService.login(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
//        User user = authService.register(request);
//        return new ResponseEntity<>(user, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/me")
//    public ResponseEntity<User> getCurrentUser() {
//        User user = authService.getCurrentUser();
//        return ResponseEntity.ok(user);
//    }
}


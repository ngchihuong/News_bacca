package com.newsroom.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String bio;
    private String avatarUrl;
    private String role;
    private Instant createdAt;
    private Instant updatedAt;
}

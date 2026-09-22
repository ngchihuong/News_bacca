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
public class PublicUserProfileResponse {
    private String id;
    private String username;
    private String fullName;
    private String avatarUrl;
    private String bio;
    private boolean isJournalistVerified;
    private String journalistOrganization;
    private int followersCount;
    private int followingCount;
    private Instant createdAt;
}

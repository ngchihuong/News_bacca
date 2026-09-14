package com.newsroom.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @MongoId
    private String id;
    
    @Indexed(unique = true)
    private String username;
    
    @Indexed(unique = true)
    private String email;
    
    private String password;

    @Field(name = "full_name")
    private String fullName;
    private String phone;
    private Integer age;

    @Field(name = "avatar_url")
    private String avatarUrl;

    private String role;

    @Builder.Default
    private boolean active = false;

    @Field(name = "failed_login_attempts")
    @Builder.Default
    private int failedLoginAttempts = 0;

    @Field(name = "lockout_until")
    private Instant lockoutUntil;

    @Field(name = "refresh_token")
    private String refreshToken;

    @CreatedDate
    @Field(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    private Instant updatedAt;
}


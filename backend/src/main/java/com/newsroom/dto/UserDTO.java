package com.newsroom.dto;

import com.newsroom.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String roles;
    private Integer age;
    private String avatarUrl;
}

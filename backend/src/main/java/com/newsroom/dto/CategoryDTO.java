package com.newsroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private String id;
    
    @NotBlank(message = "Category name is required")
    private String name;
    
    private String slug;
    private String description;
    private String imageUrl;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}


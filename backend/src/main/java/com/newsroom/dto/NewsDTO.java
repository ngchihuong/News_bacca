package com.newsroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
    private String id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String slug;
    
    @NotBlank(message = "Excerpt is required")
    private String excerpt;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private String imageUrl;
    
    @NotNull(message = "Category ID is required")
    private String categoryId;
    
    private String categoryName;
    
    private List<String> tagIds;
    private List<String> tagNames;
    
    private String authorId;
    private String authorName;
    
    private int viewCount;
    private boolean featured;
    private boolean trending;
    private String status;
    
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


package com.newsroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementDTO {
    private String id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotBlank(message = "Ad type is required")
    private String adType;
    
    @NotBlank(message = "Position is required")
    private String position;
    
    @NotBlank(message = "Format is required")
    private String format;
    
    private String imageUrl;
    private String htmlContent;
    private String scriptCode;
    
    private String targetUrl;
    private boolean openInNewTab = true;
    
    private String width;
    private String height;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    private boolean active = true;
    private int priority = 0;
    
    private long impressions = 0;
    private long clicks = 0;
    
    private int displayFrequency = 1;
    private int maxDailyImpressions = 0;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


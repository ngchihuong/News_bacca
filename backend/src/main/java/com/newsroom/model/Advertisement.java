package com.newsroom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Document(collection = "advertisements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement {
    @Id
    private String id;
    
    private String title;
    private String description;
    
    // Ad Type: BANNER, SIDEBAR, IN_FEED, POPUP
    private String adType;
    
    // Position: TOP, SIDEBAR_TOP, SIDEBAR_MIDDLE, SIDEBAR_BOTTOM, IN_FEED, FOOTER
    @Indexed
    private String position;
    
    // Ad Format: IMAGE, HTML, SCRIPT (for Google Ads, FB Ads)
    private String format;
    
    // Content based on format
    private String imageUrl;      // For IMAGE format
    private String htmlContent;   // For HTML format
    private String scriptCode;    // For SCRIPT format (Google Ads, FB Ads)
    
    private String targetUrl;     // Click destination
    private boolean openInNewTab = true;
    
    // Dimensions
    private String width;         // e.g., "300px", "100%"
    private String height;        // e.g., "250px", "auto"
    
    // Scheduling
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    // Status
    private boolean active = true;
    
    // Priority (higher number = higher priority)
    private int priority = 0;
    
    // Tracking
    private long impressions = 0;
    private long clicks = 0;
    
    // Display Settings
    private int displayFrequency = 1; // Show every X page views
    private int maxDailyImpressions = 0; // 0 = unlimited
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}


package com.newsroom.model;

import com.newsroom.enums.AdFormat;
import com.newsroom.enums.AdType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "advertisements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Advertisement {
    
    @MongoId
    private String id;
    
    private String title;
    
    private String description;

    @Field(name = "ad_type")
    private AdType adType;

    private String position;
    
    private AdFormat format;
    
    @Field(name = "image_url")
    private String imageUrl;
    
    @Field(name = "html_content")
    private String htmlContent;
    
    @Field(name = "script_code")
    private String scriptCode;
    
    @Field(name = "target_url")
    private String targetUrl;
    
    @Field(name = "open_in_new_tab")
    private Boolean openInNewTab = true;
    
    private String width;
    
    private String height;
    
    @Field(name = "start_date")
    private LocalDateTime startDate;
    
    @Field(name = "end_date")
    private LocalDateTime endDate;
    
    private Boolean active = true;
    
    private Integer priority = 0;
    
    private Long impressions = 0L;
    
    private Long clicks = 0L;
    
    @Field(name = "display_frequency")
    private Integer displayFrequency = 1; //tần suất hiển thị
    
    @Field(name = "max_daily_impressions")
    private Integer maxDailyImpressions = 0; //số lần hiển thị tối đa hàng ngày

    @CreatedDate
    @Field(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    private Instant updatedAt;
}
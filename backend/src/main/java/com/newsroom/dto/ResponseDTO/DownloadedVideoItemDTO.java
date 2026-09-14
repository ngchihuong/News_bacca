package com.newsroom.dto.ResponseDTO;

import com.newsroom.enums.SocialPlatform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadedVideoItemDTO {
    private String videoId;
    private String title;
    private SocialPlatform platform;
    private String originalUrl;
    private String minioPath;
    private Long likes;
    private Long comments;
    private Long shares;
    private Long views;
}

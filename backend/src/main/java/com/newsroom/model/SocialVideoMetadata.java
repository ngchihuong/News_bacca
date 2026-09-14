package com.newsroom.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialVideoMetadata {
    private String videoId;
    private String title;
    private String sourceUrl;
    private String directMediaUrl;
    private long likes;
    private long comments;
    private long shares;
    private long views;
}

package com.newsroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsStatsDTO {
    private long totalNews;
    private long publishedNews;
    private long draftNews;
    private long featuredNews;
}


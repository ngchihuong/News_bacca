package com.newsroom.service.implement.downloadCollectionsImpl.fetcher;

import com.newsroom.dto.DownloadVideoRequestDTO;
import com.newsroom.enums.SocialPlatform;
import com.newsroom.model.SocialVideoMetadata;
import com.newsroom.service.downloadCollecttionService.SocialVideoFetcher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TikTok public metadata is limited without partner API access.
 * This fetcher treats sourceUrl as a profile or single video URL for yt-dlp based download.
 */
@Component
public class TikTokVideoFetcher implements SocialVideoFetcher {

    @Override
    public SocialPlatform getPlatform() {
        return SocialPlatform.TIKTOK;
    }

    @Override
    public List<SocialVideoMetadata> fetchVideos(DownloadVideoRequestDTO request) {
        return List.of(SocialVideoMetadata.builder()
                .videoId(extractId(request.getSourceUrl()))
                .title("TikTok video")
                .sourceUrl(request.getSourceUrl())
                .likes(0L)
                .comments(0L)
                .shares(0L)
                .views(0L)
                .build());
    }

    private String extractId(String sourceUrl) {
        if (sourceUrl == null || sourceUrl.isBlank()) {
            return "unknown";
        }
        String[] parts = sourceUrl.replaceAll("/+$", "").split("/");
        return parts[parts.length - 1];
    }
}

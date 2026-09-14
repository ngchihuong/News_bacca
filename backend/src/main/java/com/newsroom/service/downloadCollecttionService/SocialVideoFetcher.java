package com.newsroom.service.downloadCollecttionService;

import com.newsroom.dto.DownloadVideoRequestDTO;
import com.newsroom.enums.SocialPlatform;
import com.newsroom.model.SocialVideoMetadata;

import java.util.List;

public interface SocialVideoFetcher {
    SocialPlatform getPlatform();

    List<SocialVideoMetadata> fetchVideos(DownloadVideoRequestDTO request);
}

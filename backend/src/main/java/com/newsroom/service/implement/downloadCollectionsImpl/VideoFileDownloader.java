package com.newsroom.service.implement.downloadCollectionsImpl;

import com.newsroom.dto.DownloadVideoRequestDTO;
import com.newsroom.model.SocialVideoMetadata;

public interface VideoFileDownloader {
    String downloadToTempFile(SocialVideoMetadata metadata, DownloadVideoRequestDTO request);
}

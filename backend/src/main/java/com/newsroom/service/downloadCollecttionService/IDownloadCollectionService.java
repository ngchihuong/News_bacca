package com.newsroom.service.downloadCollecttionService;

import com.newsroom.dto.DownloadVideoRequestDTO;
import com.newsroom.dto.ResponseDTO.DownloadVideoResponseDTO;

public interface IDownloadCollectionService {
    DownloadVideoResponseDTO downloadVideos(DownloadVideoRequestDTO request);
}

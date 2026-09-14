package com.newsroom.service.implement.downloadCollectionsImpl;

import com.newsroom.dto.DownloadVideoRequestDTO;
import com.newsroom.dto.ResponseDTO.DownloadVideoResponseDTO;
import com.newsroom.dto.ResponseDTO.DownloadedVideoItemDTO;
import com.newsroom.model.SocialVideoMetadata;
import com.newsroom.service.MinioService;
import com.newsroom.service.downloadCollecttionService.IDownloadCollectionService;
import com.newsroom.service.downloadCollecttionService.SocialVideoFetcher;
import com.newsroom.service.implement.downloadCollectionsImpl.fetcher.SocialVideoFetcherFactory;
import com.newsroom.service.implement.downloadCollectionsImpl.support.SocialVideoFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DownloadCollectionServiceImpl implements IDownloadCollectionService {

    private final SocialVideoFetcherFactory fetcherFactory;
    private final VideoFileDownloader videoFileDownloader;
    private final MinioService minioService;

    @Override
    public DownloadVideoResponseDTO downloadVideos(DownloadVideoRequestDTO request) {
        SocialVideoFetcher fetcher = fetcherFactory.getFetcher(request.getPlatform());
        List<SocialVideoMetadata> discoveredVideos = fetcher.fetchVideos(request);
        List<SocialVideoMetadata> matchedVideos = SocialVideoFilter.filterByEngagement(discoveredVideos, request);

        List<DownloadedVideoItemDTO> downloadedItems = new ArrayList<>();
        int failedCount = 0;

        for (SocialVideoMetadata video : matchedVideos) {
            try {
                String tempFilePath = videoFileDownloader.downloadToTempFile(video, request);
                Path tempPath = Path.of(tempFilePath);
                String minioPath = minioService.uploadLocalFileToMinio(tempPath, "video/mp4");
                Files.deleteIfExists(tempPath);

                downloadedItems.add(DownloadedVideoItemDTO.builder()
                        .videoId(video.getVideoId())
                        .title(video.getTitle())
                        .platform(request.getPlatform())
                        .originalUrl(video.getSourceUrl())
                        .minioPath(minioPath)
                        .likes(video.getLikes())
                        .comments(video.getComments())
                        .shares(video.getShares())
                        .views(video.getViews())
                        .build());
            } catch (Exception ex) {
                failedCount++;
                log.error("Failed to download and store video {}", video.getVideoId(), ex);
            }
        }

        return DownloadVideoResponseDTO.builder()
                .platform(request.getPlatform())
                .totalFound(discoveredVideos.size())
                .totalMatched(matchedVideos.size())
                .totalDownloaded(downloadedItems.size())
                .totalFailed(failedCount)
                .videos(downloadedItems)
                .build();
    }
}

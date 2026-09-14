package com.newsroom.service.implement.downloadCollectionsImpl.support;

import com.newsroom.dto.DownloadVideoRequestDTO;
import com.newsroom.model.SocialVideoMetadata;

import java.util.List;
import java.util.stream.Stream;

public final class SocialVideoFilter {

    private SocialVideoFilter() {
    }

    public static List<SocialVideoMetadata> filterByEngagement(
            List<SocialVideoMetadata> videos,
            DownloadVideoRequestDTO request) {
        return videos.stream()
                .filter(video -> matchesEngagement(video, request))
                .limit(request.getMaxResults())
                .toList();
    }

    public static boolean matchesEngagement(SocialVideoMetadata video, DownloadVideoRequestDTO request) {
        return video.getLikes() >= request.getMinLikes()
                && video.getComments() >= request.getMinComments()
                && video.getShares() >= request.getMinShares()
                && video.getViews() >= request.getMinViews();
    }

    public static long parseCount(String value) {
        if (value == null || value.isBlank()) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return 0L;
        }
    }

    public static String firstNonBlank(String... values) {
        return Stream.of(values)
                .filter(value -> value != null && !value.isBlank())
                .findFirst()
                .orElse(null);
    }
}

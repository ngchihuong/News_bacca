package com.newsroom.service.implement.downloadCollectionsImpl.fetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.newsroom.config.SocialMediaProperties;
import com.newsroom.config.exceptions.NewsCommonException;
import com.newsroom.dto.DownloadVideoRequestDTO;
import com.newsroom.enums.SocialPlatform;
import com.newsroom.model.SocialVideoMetadata;
import com.newsroom.service.downloadCollecttionService.SocialVideoFetcher;
import com.newsroom.service.implement.downloadCollectionsImpl.support.SocialVideoFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class YouTubeVideoFetcher implements SocialVideoFetcher {

    private static final Pattern CHANNEL_ID_PATTERN = Pattern.compile("(?:channel/|channelId=)([a-zA-Z0-9_-]+)");
    private static final Pattern PLAYLIST_ID_PATTERN = Pattern.compile("(?:list=)([a-zA-Z0-9_-]+)");

    private final RestClient restClient;
    private final SocialMediaProperties socialMediaProperties;

    @Override
    public SocialPlatform getPlatform() {
        return SocialPlatform.YOUTUBE;
    }

    @Override
    public List<SocialVideoMetadata> fetchVideos(DownloadVideoRequestDTO request) {
        String apiKey = socialMediaProperties.getYoutube().getApiKey();
        if (apiKey == null || apiKey.isBlank()) {
            throw new NewsCommonException("error.social.youtube.api_key_missing");
        }

        String channelId = extractChannelId(request.getSourceUrl());
        String playlistId = extractPlaylistId(request.getSourceUrl());

        List<String> videoIds = new ArrayList<>();
        if (playlistId != null) {
            videoIds.addAll(fetchPlaylistVideoIds(playlistId, apiKey, request.getMaxResults()));
        } else if (channelId != null) {
            videoIds.addAll(searchVideoIds("channelId", channelId, apiKey, request.getMaxResults()));
        } else {
            videoIds.addAll(searchVideoIds("q", request.getSourceUrl(), apiKey, request.getMaxResults()));
        }

        return fetchVideoDetails(videoIds, apiKey);
    }

    private List<String> fetchPlaylistVideoIds(String playlistId, String apiKey, int maxResults) {
        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("www.googleapis.com")
                        .path("/youtube/v3/playlistItems")
                        .queryParam("part", "contentDetails")
                        .queryParam("playlistId", playlistId)
                        .queryParam("maxResults", Math.min(maxResults, 50))
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .body(JsonNode.class);

        List<String> ids = new ArrayList<>();
        if (response != null && response.has("items")) {
            for (JsonNode item : response.get("items")) {
                ids.add(item.path("contentDetails").path("videoId").asText());
            }
        }
        return ids;
    }

    private List<String> searchVideoIds(String paramName, String paramValue, String apiKey, int maxResults) {
        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("www.googleapis.com")
                        .path("/youtube/v3/search")
                        .queryParam("part", "id")
                        .queryParam("type", "video")
                        .queryParam(paramName, paramValue)
                        .queryParam("maxResults", Math.min(maxResults, 50))
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .body(JsonNode.class);

        List<String> ids = new ArrayList<>();
        if (response != null && response.has("items")) {
            for (JsonNode item : response.get("items")) {
                ids.add(item.path("id").path("videoId").asText());
            }
        }
        return ids;
    }

    private List<SocialVideoMetadata> fetchVideoDetails(List<String> videoIds, String apiKey) {
        if (videoIds.isEmpty()) {
            return List.of();
        }

        String joinedIds = String.join(",", videoIds);
        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("www.googleapis.com")
                        .path("/youtube/v3/videos")
                        .queryParam("part", "snippet,statistics")
                        .queryParam("id", joinedIds)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .body(JsonNode.class);

        List<SocialVideoMetadata> videos = new ArrayList<>();
        if (response == null || !response.has("items")) {
            return videos;
        }

        for (JsonNode item : response.get("items")) {
            String videoId = item.path("id").asText();
            JsonNode statistics = item.path("statistics");
            JsonNode snippet = item.path("snippet");

            videos.add(SocialVideoMetadata.builder()
                    .videoId(videoId)
                    .title(snippet.path("title").asText("Untitled"))
                    .sourceUrl("https://www.youtube.com/watch?v=" + videoId)
                    .likes(SocialVideoFilter.parseCount(statistics.path("likeCount").asText(null)))
                    .comments(SocialVideoFilter.parseCount(statistics.path("commentCount").asText(null)))
                    .shares(0L)
                    .views(SocialVideoFilter.parseCount(statistics.path("viewCount").asText(null)))
                    .build());
        }
        return videos;
    }

    private String extractChannelId(String sourceUrl) {
        Matcher matcher = CHANNEL_ID_PATTERN.matcher(sourceUrl);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String extractPlaylistId(String sourceUrl) {
        Matcher matcher = PLAYLIST_ID_PATTERN.matcher(sourceUrl);
        return matcher.find() ? matcher.group(1) : null;
    }
}

package com.newsroom.service.implement.downloadCollectionsImpl.fetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.newsroom.config.SocialMediaProperties;
import com.newsroom.config.exceptions.NewsCommonException;
import com.newsroom.dto.DownloadVideoRequestDTO;
import com.newsroom.enums.SocialPlatform;
import com.newsroom.model.SocialVideoMetadata;
import com.newsroom.service.downloadCollecttionService.SocialVideoFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstagramVideoFetcher implements SocialVideoFetcher {

    private static final Set<String> VIDEO_MEDIA_TYPES = Set.of("VIDEO", "REELS");

    private final RestClient restClient;
    private final SocialMediaProperties socialMediaProperties;

    @Override
    public SocialPlatform getPlatform() {
        return SocialPlatform.INSTAGRAM;
    }

    @Override
    public List<SocialVideoMetadata> fetchVideos(DownloadVideoRequestDTO request) {
        String accessToken = resolveAccessToken(request);
        String userId = resolveUserId(request.getSourceUrl());

        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("graph.facebook.com")
                        .path("/v19.0/" + userId + "/media")
                        .queryParam("fields", "id,caption,media_type,media_url,permalink,like_count,comments_count")
                        .queryParam("limit", Math.min(request.getMaxResults(), 100))
                        .queryParam("access_token", accessToken)
                        .build())
                .retrieve()
                .body(JsonNode.class);

        List<SocialVideoMetadata> videos = new ArrayList<>();
        if (response == null || !response.has("data")) {
            return videos;
        }

        for (JsonNode item : response.get("data")) {
            String mediaType = item.path("media_type").asText();
            if (!VIDEO_MEDIA_TYPES.contains(mediaType)) {
                continue;
            }

            String mediaUrl = item.path("media_url").asText(null);
            if (mediaUrl == null || mediaUrl.isBlank()) {
                continue;
            }

            videos.add(SocialVideoMetadata.builder()
                    .videoId(item.path("id").asText())
                    .title(item.path("caption").asText("Instagram video"))
                    .sourceUrl(item.path("permalink").asText(mediaUrl))
                    .directMediaUrl(mediaUrl)
                    .likes(item.path("like_count").asLong(0))
                    .comments(item.path("comments_count").asLong(0))
                    .shares(0L)
                    .views(0L)
                    .build());
        }
        return videos;
    }

    private String resolveAccessToken(DownloadVideoRequestDTO request) {
        String token = request.getAccessToken();
        if (token == null || token.isBlank()) {
            token = socialMediaProperties.getInstagram().getAccessToken();
        }
        if (token == null || token.isBlank()) {
            throw new NewsCommonException("error.social.instagram.access_token_missing");
        }
        return token;
    }

    private String resolveUserId(String sourceUrl) {
        if (sourceUrl == null || sourceUrl.isBlank()) {
            throw new NewsCommonException("error.social.instagram.user_id_missing");
        }
        if (sourceUrl.startsWith("http")) {
            String[] parts = sourceUrl.replaceAll("/+$", "").split("/");
            return parts[parts.length - 1];
        }
        return sourceUrl;
    }
}

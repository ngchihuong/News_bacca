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

@Slf4j
@Component
@RequiredArgsConstructor
public class FacebookVideoFetcher implements SocialVideoFetcher {

    private final RestClient restClient;
    private final SocialMediaProperties socialMediaProperties;

    @Override
    public SocialPlatform getPlatform() {
        return SocialPlatform.FACEBOOK;
    }

    @Override
    public List<SocialVideoMetadata> fetchVideos(DownloadVideoRequestDTO request) {
        String accessToken = resolveAccessToken(request);
        String pageId = resolvePageId(request.getSourceUrl());

        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("graph.facebook.com")
                        .path("/v19.0/" + pageId + "/videos")
                        .queryParam("fields", "id,title,source,permalink_url,likes.summary(true),comments.summary(true),shares")
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
            String directUrl = item.path("source").asText(null);
            if (directUrl == null || directUrl.isBlank()) {
                continue;
            }

            videos.add(SocialVideoMetadata.builder()
                    .videoId(item.path("id").asText())
                    .title(item.path("title").asText("Facebook video"))
                    .sourceUrl(item.path("permalink_url").asText(directUrl))
                    .directMediaUrl(directUrl)
                    .likes(item.path("likes").path("summary").path("total_count").asLong(0))
                    .comments(item.path("comments").path("summary").path("total_count").asLong(0))
                    .shares(item.path("shares").path("count").asLong(0))
                    .views(0L)
                    .build());
        }
        return videos;
    }

    private String resolveAccessToken(DownloadVideoRequestDTO request) {
        String token = request.getAccessToken();
        if (token == null || token.isBlank()) {
            token = socialMediaProperties.getFacebook().getAccessToken();
        }
        if (token == null || token.isBlank()) {
            throw new NewsCommonException("error.social.facebook.access_token_missing");
        }
        return token;
    }

    private String resolvePageId(String sourceUrl) {
        if (sourceUrl == null || sourceUrl.isBlank()) {
            throw new NewsCommonException("error.social.facebook.page_id_missing");
        }
        if (sourceUrl.startsWith("http")) {
            String[] parts = sourceUrl.replaceAll("/+$", "").split("/");
            return parts[parts.length - 1];
        }
        return sourceUrl;
    }
}

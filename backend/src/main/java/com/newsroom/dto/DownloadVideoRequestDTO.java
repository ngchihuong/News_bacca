package com.newsroom.dto;

import com.newsroom.enums.SocialPlatform;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadVideoRequestDTO {

    @NotNull(message = "Platform is required")
    private SocialPlatform platform;

    /**
     * Channel URL, page URL, profile URL, playlist URL, hashtag or search keyword.
     */
    @NotBlank(message = "Source URL or identifier is required")
    private String sourceUrl;

    @Min(0)
    @Builder.Default
    private Long minLikes = 0L;

    @Min(0)
    @Builder.Default
    private Long minComments = 0L;

    @Min(0)
    @Builder.Default
    private Long minShares = 0L;

    @Min(0)
    @Builder.Default
    private Long minViews = 0L;

    @Min(1)
    @Builder.Default
    private Integer maxResults = 50;

    /**
     * Optional platform access token. Falls back to application config when omitted.
     */
    private String accessToken;
}

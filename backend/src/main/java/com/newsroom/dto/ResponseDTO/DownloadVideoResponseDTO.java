package com.newsroom.dto.ResponseDTO;

import com.newsroom.enums.SocialPlatform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadVideoResponseDTO {
    private SocialPlatform platform;
    private int totalFound;
    private int totalMatched;
    private int totalDownloaded;
    private int totalFailed;
    private List<DownloadedVideoItemDTO> videos;
}

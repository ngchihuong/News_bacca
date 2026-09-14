package com.newsroom.controller.publics.DownloadCollections;

import com.newsroom.dto.DownloadVideoRequestDTO;
import com.newsroom.dto.ResponseDTO.BaseOutput;
import com.newsroom.dto.ResponseDTO.DownloadVideoResponseDTO;
import com.newsroom.enums.ResponseStatus;
import com.newsroom.service.downloadCollecttionService.IDownloadCollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/download")
@RequiredArgsConstructor
public class DownloadCollectionController {

    private final IDownloadCollectionService downloadCollectionService;

    @PostMapping("/video/download")
    public ResponseEntity<BaseOutput<DownloadVideoResponseDTO>> downloadVideo(
            @Valid @RequestBody DownloadVideoRequestDTO request) {
        DownloadVideoResponseDTO response = downloadCollectionService.downloadVideos(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseOutput.<DownloadVideoResponseDTO>builder()
                        .status(ResponseStatus.SUCCESS)
                        .message("Videos downloaded successfully")
                        .data(response)
                        .build());
    }
}

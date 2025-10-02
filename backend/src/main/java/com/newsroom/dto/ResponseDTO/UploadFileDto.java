package com.newsroom.dto.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class UploadFileDto {
    private String id;
    private String qrCodeUrl;
    private LocalDateTime uploadedAt;
}

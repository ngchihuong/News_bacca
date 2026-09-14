package com.newsroom.service;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;

public interface MinioService {
    String getBucket();

    String uploadToMinio(@NonNull MultipartFile file);

    String uploadLocalFileToMinio(@NonNull Path filePath, @NonNull String contentType);

    String getImageThumbnailFromMinio(@NonNull String path);

    String getImageFromMinio(@NonNull String path);

    void deleteFromMinio(@NonNull String path);

    InputStream downloadFromMinio(@NonNull String path);
}

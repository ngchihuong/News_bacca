package com.newsroom.service.implement.downloadCollectionsImpl;

import com.newsroom.config.SocialMediaProperties;
import com.newsroom.config.exceptions.NewsCommonException;
import com.newsroom.dto.DownloadVideoRequestDTO;
import com.newsroom.model.SocialVideoMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoFileDownloaderImpl implements VideoFileDownloader {

    private final RestClient restClient;
    private final SocialMediaProperties socialMediaProperties;

    @Override
    public String downloadToTempFile(SocialVideoMetadata metadata, DownloadVideoRequestDTO request) {
        Path tempDir = createTempDirectory();
        try {
            if (metadata.getDirectMediaUrl() != null && !metadata.getDirectMediaUrl().isBlank()) {
                return downloadDirectMedia(metadata.getDirectMediaUrl(), tempDir, metadata.getVideoId());
            }
            return downloadWithYtDlp(metadata.getSourceUrl(), tempDir, metadata.getVideoId());
        } catch (IOException | InterruptedException ex) {
            log.error("Failed to download video {}", metadata.getVideoId(), ex);
            throw new NewsCommonException("error.social.video.download_failed");
        }
    }

    private Path createTempDirectory() {
        try {
            return Files.createTempDirectory("social-video-");
        } catch (IOException ex) {
            throw new NewsCommonException("error.social.video.temp_dir_failed");
        }
    }

    private String downloadDirectMedia(String mediaUrl, Path tempDir, String videoId) throws IOException {
        Path targetFile = tempDir.resolve(sanitizeFileName(videoId) + ".mp4");
        try (InputStream inputStream = restClient.get()
                .uri(mediaUrl)
                .retrieve()
                .body(InputStream.class)) {
            if (inputStream == null) {
                throw new NewsCommonException("error.social.video.download_failed");
            }
            Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return targetFile.toString();
    }

    private String downloadWithYtDlp(String sourceUrl, Path tempDir, String videoId)
            throws IOException, InterruptedException {
        String outputTemplate = tempDir.resolve(sanitizeFileName(videoId) + ".%(ext)s").toString();
        ProcessBuilder processBuilder = new ProcessBuilder(
                socialMediaProperties.getYtDlpPath(),
                "-f", "best[ext=mp4]/best",
                "-o", outputTemplate,
                "--no-playlist",
                sourceUrl
        );
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new NewsCommonException("error.social.video.ytdlp_failed");
        }

        try (Stream<Path> files = Files.list(tempDir)) {
            return files
                    .filter(Files::isRegularFile)
                    .max(Comparator.comparingLong(path -> path.toFile().lastModified()))
                    .map(Path::toString)
                    .orElseThrow(() -> new NewsCommonException("error.social.video.download_failed"));
        }
    }

    private String sanitizeFileName(String value) {
        return value.replaceAll("[^a-zA-Z0-9_-]", "_");
    }
}

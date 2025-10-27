package com.newsroom.controller.publics;

import com.newsroom.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicImageController {
    private final MinioService minioService;

    @GetMapping("/view/image/{image}")
    public ResponseEntity<Void> viewImage(@PathVariable String image) {
        try {
            if (!StringUtils.hasText(image)) {
                return ResponseEntity.notFound().build();
            }

            String presignedUrl = minioService.getImageFromMinio(image);

            log.error("Minio Presigned Url : {}", presignedUrl);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(presignedUrl)).build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

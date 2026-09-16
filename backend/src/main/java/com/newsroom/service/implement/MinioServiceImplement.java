package com.newsroom.service.implement;

import com.newsroom.commons.Constants;
import com.newsroom.config.exceptions.NewsCommonException;
import com.newsroom.service.MinioService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioServiceImplement implements MinioService {
    private final MinioClient minioClient;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    @Override
    public String getBucket() {
        return this.bucketName;
    }

    @Override
    @CacheEvict(value = "image:path", allEntries = true)
    public String uploadToMinio(@NonNull MultipartFile file) {
        if (file.isEmpty()) {
            log.error("ERROR file is empty");
            throw new NewsCommonException(Constants.ERROR.FILE.EMPTY);
        }
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            String formatedFileName = this.formatFileName(file.getOriginalFilename());

            PutObjectArgs putObjectArgs =
                    PutObjectArgs.builder().bucket(bucketName).object(formatedFileName).stream(
                            file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType()).build();
            minioClient.putObject(putObjectArgs);

            log.info("Uploaded file to minio, file: {}", formatedFileName);
            return formatedFileName;
        } catch (
                InvalidKeyException e) {
            log.error("ERROR InvalidKeyException when upload file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INVALID_KEY);
        } catch (
                FileAlreadyExistsException e) {
            log.error("ERROR FileAlreadyExistsException when upload file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.FILE_ALREADY_EXISTS);
        } catch (
                ErrorResponseException e) {
            log.error("ERROR ErrorResponseException when upload file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.RESPONSE);
        } catch (
                InsufficientDataException e) {
            log.error("ERROR InsufficientDataException when upload file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INSUFFICIENT_DATA);
        } catch (
                InternalException e) {
            log.error("ERROR InternalException when upload file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INTERNAL_EXCEPTION);
        } catch (
                InvalidResponseException e) {
            log.error("ERROR InvalidResponseException when upload file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INVALID_RESPONSE);
        } catch (
                IOException e) {
            log.error("ERROR IOException when upload file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.IO);
        } catch (
                NoSuchAlgorithmException e) {
            log.error("ERROR NoSuchAlgorithmException when upload file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.NO_SUCH_ALGORITHM);
        } catch (
                ServerException e) {
            log.error("ERROR ServerException when upload file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.SERVER);
        } catch (XmlParserException e) {
            log.error("ERROR XmlParserException when upload file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.XML_PARSER);
        } catch (Exception e) {
            log.error("ERROR when upload file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.EXTERNAL);
        }
    }

    @Override
    public String getImageThumbnailFromMinio(@NonNull String path) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .method(Method.GET)
                            .object(Constants.FILE.THUMBNAIL_NAME_PREFIX + path)
                            .build());
        } catch (InvalidKeyException e) {
            log.error("ERROR InvalidKeyException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INVALID_KEY);
        } catch (FileAlreadyExistsException e) {
            log.error("ERROR FileAlreadyExistsException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.FILE_ALREADY_EXISTS);
        } catch (ErrorResponseException e) {
            log.error("ERROR ErrorResponseException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.RESPONSE);
        } catch (InsufficientDataException e) {
            log.error("ERROR InsufficientDataException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INSUFFICIENT_DATA);
        } catch (InternalException e) {
            log.error("ERROR InternalException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INTERNAL_EXCEPTION);
        } catch (InvalidResponseException e) {
            log.error("ERROR InvalidResponseException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INVALID_RESPONSE);
        } catch (IOException e) {
            log.error("ERROR IOException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.IO);
        } catch (NoSuchAlgorithmException e) {
            log.error("ERROR NoSuchAlgorithmException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.NO_SUCH_ALGORITHM);
        } catch (ServerException e) {
            log.error("ERROR ServerException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.SERVER);
        } catch (XmlParserException e) {
            log.error("ERROR XmlParserException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.XML_PARSER);
        } catch (Exception e) {
            log.error("ERROR when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.EXTERNAL);
        }
    }

    @Cacheable("image:path")
    @Override
    public String getImageFromMinio(@NonNull String path) {
        try {
            String url =
                    minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .bucket(bucketName)
                                    .method(Method.GET)
                                    .object(path)
                                    .build());
            if (url.isEmpty()) {
                return null;
            }
            return url;
        } catch (InvalidKeyException e) {
            log.error("ERROR InvalidKeyException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INVALID_KEY);
        } catch (FileAlreadyExistsException e) {
            log.error("ERROR FileAlreadyExistsException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.FILE_ALREADY_EXISTS);
        } catch (ErrorResponseException e) {
            log.error("ERROR ErrorResponseException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.RESPONSE);
        } catch (InsufficientDataException e) {
            log.error("ERROR InsufficientDataException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INSUFFICIENT_DATA);
        } catch (InternalException e) {
            log.error("ERROR InternalException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INTERNAL_EXCEPTION);
        } catch (InvalidResponseException e) {
            log.error("ERROR InvalidResponseException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INVALID_RESPONSE);
        } catch (IOException e) {
            log.error("ERROR IOException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.IO);
        } catch (NoSuchAlgorithmException e) {
            log.error("ERROR NoSuchAlgorithmException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.NO_SUCH_ALGORITHM);
        } catch (ServerException e) {
            log.error("ERROR ServerException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.SERVER);
        } catch (XmlParserException e) {
            log.error("ERROR XmlParserException when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.XML_PARSER);
        } catch (Exception e) {
            log.error("ERROR when get url file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.EXTERNAL);
        }
    }

    @CacheEvict(value = "image:path", allEntries = true)
    @Override
    public void deleteFromMinio(@NonNull String path) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(path).build());
            log.info("deleted from minio client, file: {}", path);
        } catch (InvalidKeyException e) {
            log.error("ERROR InvalidKeyException when delete file", e);
        } catch (FileAlreadyExistsException e) {
            log.error("ERROR FileAlreadyExistsException when delete file", e);
        } catch (ErrorResponseException e) {
            log.error("ERROR ErrorResponseException when delete file", e);
        } catch (InsufficientDataException e) {
            log.error("ERROR InsufficientDataException when delete file", e);
        } catch (InternalException e) {
            log.error("ERROR InternalException when delete file", e);
        } catch (InvalidResponseException e) {
            log.error("ERROR InvalidResponseException when delete file", e);
        } catch (IOException e) {
            log.error("ERROR IOException when delete file", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("ERROR NoSuchAlgorithmException when delete file", e);
        } catch (ServerException e) {
            log.error("ERROR ServerException when delete file", e);
        } catch (XmlParserException e) {
            log.error("ERROR XmlParserException when delete file", e);
        } catch (Exception e) {
            log.error("ERROR when delete file", e);
        }
    }

    @Override
    public InputStream downloadFromMinio(@NonNull String path) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(path).build());
        } catch (InvalidKeyException e) {
            log.error("ERROR InvalidKeyException when get file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INVALID_KEY);
        } catch (FileAlreadyExistsException e) {
            log.error("ERROR FileAlreadyExistsException when get file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.FILE_ALREADY_EXISTS);
        } catch (ErrorResponseException e) {
            log.error("ERROR ErrorResponseException when get file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.RESPONSE);
        } catch (InsufficientDataException e) {
            log.error("ERROR InsufficientDataException when get file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INSUFFICIENT_DATA);
        } catch (InternalException e) {
            log.error("ERROR InternalException when get file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INTERNAL_EXCEPTION);
        } catch (InvalidResponseException e) {
            log.error("ERROR InvalidResponseException when get file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.INVALID_RESPONSE);
        } catch (IOException e) {
            log.error("ERROR IOException when get file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.IO);
        } catch (NoSuchAlgorithmException e) {
            log.error("ERROR NoSuchAlgorithmException when get file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.NO_SUCH_ALGORITHM);
        } catch (ServerException e) {
            log.error("ERROR ServerException when get file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.SERVER);
        } catch (XmlParserException e) {
            log.error("ERROR XmlParserException when get file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.XML_PARSER);
        } catch (Exception e) {
            log.error("ERROR when get file", e);
            throw new NewsCommonException(Constants.ERROR.MINIO.EXTERNAL);
        }
    }

    private String formatFileName(String originalFileName) {
        if (originalFileName == null || originalFileName.isBlank()) {
            originalFileName = "avatar_" + System.currentTimeMillis() + ".jpg";
        }
        int dotIndex = originalFileName.lastIndexOf(".");
        String baseName;
        String extension;
        if (dotIndex >= 0) {
            baseName = originalFileName.substring(0, dotIndex);
            extension = originalFileName.substring(dotIndex);
        } else {
            baseName = originalFileName;
            extension = ".jpg";
        }

        // Only keep alphanumeric, _ and -
        baseName = baseName.replaceAll("[^a-zA-Z0-9]", "_");
        long timestamp = System.currentTimeMillis();
        return baseName + "-" + timestamp + extension;
    }
}

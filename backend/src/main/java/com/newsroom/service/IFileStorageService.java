package com.newsroom.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface IFileStorageService {
    String storeFile(MultipartFile file);
    void deleteFile(String fileName);
    Path getFilePath(String fileName);
}


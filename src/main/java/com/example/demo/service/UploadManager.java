package com.example.demo.service;

import com.example.demo.entity.Upload;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface UploadManager {
    Upload upload(MultipartFile file) throws Exception;

    void remove(Upload upload) throws Exception;

    Path getAbsolutePath(Upload upload);
}

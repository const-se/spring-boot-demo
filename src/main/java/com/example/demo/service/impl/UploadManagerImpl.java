package com.example.demo.service.impl;

import com.example.demo.entity.Upload;
import com.example.demo.repository.UploadRepository;
import com.example.demo.service.UploadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadManagerImpl implements UploadManager {
    private UploadRepository uploadRepository;
    private String uploadsPath;

    public UploadManagerImpl(
            @Autowired UploadRepository uploadRepository,
            @Value("${app.uploads.path}") String uploadsPath
    ) {
        this.uploadRepository = uploadRepository;
        this.uploadsPath = uploadsPath;
    }

    @Override
    public Upload upload(MultipartFile file) throws Exception {
        String originalFileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
        String filename = generateUniqueFileName(originalFileName);

        file.transferTo(getUploadsPath().resolve(filename).toFile());

        Upload upload = new Upload();
        upload.setFilename(filename);
        upload.setOriginalFilename(originalFileName);
        upload.setContentType(file.getContentType());

        uploadRepository.save(upload);
        uploadRepository.flush();

        return upload;
    }

    @Override
    public void remove(Upload upload) throws Exception {
        File file = getUploadsPath().resolve(upload.getFilename()).toFile();
        file.delete();

        uploadRepository.delete(upload);
        uploadRepository.flush();
    }

    @Override
    public Path getAbsolutePath(Upload upload) {
        return getUploadsPath().resolve(upload.getFilename());
    }

    private Path getUploadsPath() {
        return Paths.get(uploadsPath).toAbsolutePath();
    }

    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + getFileExtension(originalFilename);
    }

    private String getFileExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index >= 0) {
            return filename.substring(index);
        }

        return "";
    }
}

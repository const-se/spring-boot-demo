package com.example.demo.controller;

import com.example.demo.entity.Upload;
import com.example.demo.exception.HttpNotFoundException;
import com.example.demo.service.UploadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@Controller
@RequestMapping("/uploads")
public class UploadsController {
    @Autowired
    private UploadManager uploadManager;

    @GetMapping("/{uploadId}")
    @ResponseBody
    public ResponseEntity<Resource> upload(@PathVariable("uploadId") Upload upload) throws MalformedURLException {
        Resource file = new UrlResource(uploadManager.getAbsolutePath(upload).toUri());

        if (!file.exists() || !file.isReadable()) {
            throw new HttpNotFoundException();
        }

        return ResponseEntity.ok().body(file);
    }
}

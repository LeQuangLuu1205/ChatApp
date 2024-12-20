package com.intern.ChatApp.service.impl;

import com.intern.ChatApp.enums.ErrorCode;
import com.intern.ChatApp.exception.AppException;
import com.intern.ChatApp.service.FileService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService {
    @Value("${fileUpload.rootPath2}")
    private String rootPath;
    private Path root;
    @PostConstruct
    public void init() {
        try {
            root = Paths.get(rootPath);
            if (Files.notExists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }
    @Override
    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot save empty file: " + file.getOriginalFilename());
        }

        try {
            String filename = file.getOriginalFilename();
            Path targetPath = this.root.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return filename; // Chỉ trả về tên file
        } catch (IOException e) {
            throw new RuntimeException("Could not save file: " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Resource loadFile(String fileName) {
        try {
            Path file = root.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Could not read the file: " + fileName);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }
}

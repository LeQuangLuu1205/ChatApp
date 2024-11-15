package com.intern.ChatApp.service.impl;

import com.intern.ChatApp.service.ImageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${fileUpload.rootPath}")
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
    public String saveImageFromBase64(String base64Image) {

        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
            String imageName = "image_" + System.currentTimeMillis() + ".png";
            Path imagePath = root.resolve(imageName);
            Files.write(imagePath, imageBytes);

            return imageName;
        } catch (IOException e) {

            throw new RuntimeException("Failed to save image", e);

        }
    }
}

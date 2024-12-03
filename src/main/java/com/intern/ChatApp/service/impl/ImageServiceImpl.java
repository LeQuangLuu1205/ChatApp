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
    @Override
    public String saveFileFromBase64(String base64File) {
        try {
            // Kiểm tra xem Base64 có chứa header không
            String base64Content = base64File.contains(",") ? base64File.split(",")[1] : base64File;

            byte[] fileBytes = Base64.getDecoder().decode(base64Content);

            // Tạo tên file với phần mở rộng phù hợp
            String fileExtension = getFileExtension(base64File);
            String fileName = "file_" + System.currentTimeMillis() + fileExtension;

            Path filePath = root.resolve(fileName);
            Files.write(filePath, fileBytes);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Base64 format", e);
        }
    }

    private String getFileExtension(String base64File) {
        // Dò tìm phần mở rộng dựa trên header Base64 (nếu có)
        if (base64File.startsWith("data:text/plain")) return ".txt";
        if (base64File.startsWith("data:image/png")) return ".png";
        if (base64File.startsWith("data:image/jpeg")) return ".jpg";
        if (base64File.startsWith("data:image/gif")) return ".gif";
        if (base64File.startsWith("data:application/pdf")) return ".pdf";
        if (base64File.startsWith("data:application/vnd.openxmlformats-officedocument.wordprocessingml.document")) return ".docx";
        return ""; // Trường hợp không tìm thấy phần mở rộng
    }


}

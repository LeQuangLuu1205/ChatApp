package com.intern.ChatApp.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Paths;

@RestController
public class FileController {

    private static final String UPLOAD_DIR = "F:/study/tryhard/uploads/"; // Đường dẫn tới thư mục lưu trữ file

    @GetMapping("/download/uploads/{filename}")
    public ResponseEntity<FileSystemResource> downloadFile(@PathVariable String filename) {
        // Đảm bảo file tồn tại trong thư mục
        File file = new File(UPLOAD_DIR + filename);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename) // Đảm bảo trình duyệt nhận dạng là file download
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // Loại content cho file binary
                .body(resource);
    }
}

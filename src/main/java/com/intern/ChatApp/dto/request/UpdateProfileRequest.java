package com.intern.ChatApp.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileRequest {
    private String name;
    private String oldPassword;
    private String newPassword;
    private MultipartFile image; 
}

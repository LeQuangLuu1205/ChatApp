package com.intern.ChatApp.dto.response;

import com.intern.ChatApp.entity.Role;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserResponse {

    private Integer id;
    private String email;
    private String name;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Role role;
}

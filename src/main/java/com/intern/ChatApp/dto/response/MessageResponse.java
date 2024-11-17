package com.intern.ChatApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

    private Integer id;
    private String messageText;
    private String fileUrl;
    private Boolean isPinned;
    private LocalDateTime createdAt;
    private UserResponse userResponse;
}

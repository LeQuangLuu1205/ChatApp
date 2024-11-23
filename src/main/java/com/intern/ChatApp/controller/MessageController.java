package com.intern.ChatApp.controller;

import com.intern.ChatApp.dto.request.MessageRequest;
import com.intern.ChatApp.dto.response.ApiResponse;
import com.intern.ChatApp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    @Autowired
    private MessageService messageService;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Integer id) {
        messageService.disableMessage(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(1000)
                        .message("Message disabled successfully")
                        .build()
        );
    }
}

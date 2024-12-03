package com.intern.ChatApp.controller;

import com.intern.ChatApp.dto.request.MessageRequest;
import com.intern.ChatApp.dto.response.ApiResponse;
import com.intern.ChatApp.dto.response.MessageResponse;
import com.intern.ChatApp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping("/{messageId}/pin")
    public ResponseEntity<ApiResponse<MessageResponse>> pinMessage(@PathVariable Integer messageId) {
        MessageResponse pinnedMessage = messageService.pinMessage(messageId);
        return ResponseEntity.ok(
                ApiResponse.<MessageResponse>builder()
                        .code(1000)
                        .message("Message pinned successfully")
                        .result(pinnedMessage)
                        .build()
        );
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessagesByRoom(@PathVariable Integer roomId) {
        return ResponseEntity.ok(
                ApiResponse.<List<MessageResponse>>builder()
                        .code(1000)
                        .message("Message pinned successfully")
                        .result(messageService.getMessagesByRoomId(roomId))
                        .build()
        );
    }
}

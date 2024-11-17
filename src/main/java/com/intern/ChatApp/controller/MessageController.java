package com.intern.ChatApp.controller;

import com.intern.ChatApp.dto.request.MessageRequest;
import com.intern.ChatApp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/{roomId}/messages")
    public ResponseEntity<String> sendMessage(
                                @PathVariable Integer roomId,
                                @RequestBody MessageRequest messageRequest) {
        messageService.sendMessage(messageRequest, roomId);
        return ResponseEntity.ok("Message sent successfully");
    }
}

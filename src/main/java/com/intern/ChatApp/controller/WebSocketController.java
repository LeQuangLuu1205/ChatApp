package com.intern.ChatApp.controller;

import com.intern.ChatApp.dto.request.MessageRequest;
import com.intern.ChatApp.service.FileService;
import com.intern.ChatApp.service.ImageService;
import com.intern.ChatApp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;


@Controller
public class WebSocketController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat/{roomId}")
    public void sendMessageToKafka(@Payload MessageRequest messageRequest,
                                   @DestinationVariable String roomId,
                                   SimpMessageHeaderAccessor headerAccessor) {

        String email = (String) headerAccessor.getSessionAttributes().get("email");
        messageRequest.setEmail(email);

        if (messageRequest.getImageBase64() != null && !messageRequest.getImageBase64().isEmpty()) {
//            String imageUrl = imageService.saveImageFromBase64(messageRequest.getImageBase64());
            String imageUrl = imageService.saveFileFromBase64(messageRequest.getImageBase64());

            messageRequest.setFileUrl(imageUrl);
        }

        messageService.sendMessage(messageRequest, messageRequest.getRoomId());
    }
}
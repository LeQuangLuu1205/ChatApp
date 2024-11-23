package com.intern.ChatApp.service;

import com.intern.ChatApp.dto.request.MessageRequest;
import com.intern.ChatApp.dto.response.MessageResponse;

public interface MessageService {
    void disableMessage(Integer messageId);
    void sendMessage(MessageRequest messageRequest, Integer roomId);
    public MessageResponse saveMessageToDatabase(MessageRequest messageRequest,Integer roomId);
}

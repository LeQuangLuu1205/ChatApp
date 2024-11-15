package com.intern.ChatApp.service;

import com.intern.ChatApp.dto.request.MessageRequest;
import com.intern.ChatApp.dto.response.MessageResponse;

public interface MessageService {
    void sendMessage(MessageRequest messageRequest, Integer roomId);
    public MessageResponse saveMessageToDatabase(MessageRequest messageRequest,Integer roomId);
}

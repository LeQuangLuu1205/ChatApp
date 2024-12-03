package com.intern.ChatApp.service;

import com.intern.ChatApp.dto.request.MessageRequest;
import com.intern.ChatApp.dto.response.MessageResponse;

import java.util.List;

public interface MessageService {
    void disableMessage(Integer messageId);
    MessageResponse pinMessage(Integer messageId);
    void sendMessage(MessageRequest messageRequest, Integer roomId);
    List<MessageResponse> getMessagesByRoomId(Integer roomId);
    public MessageResponse saveMessageToDatabase(MessageRequest messageRequest,Integer roomId);
}

package com.intern.ChatApp.config.kafka;

import com.intern.ChatApp.controller.WebSocketController;
import com.intern.ChatApp.dto.request.MessageRequest;
import com.intern.ChatApp.dto.response.MessageResponse;
import com.intern.ChatApp.service.MessageService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private WebSocketController webSocketMessageController;
    @KafkaListener(topicPattern = "chat-room-.*", groupId = "chat-app-group")
    public void onMessage(ConsumerRecord<String, MessageRequest> record) {

        String roomId = record.topic().split("-")[2];
        MessageRequest messageRequest = record.value();

        MessageResponse message = messageService.saveMessageToDatabase(messageRequest, Integer.parseInt(roomId));
        messagingTemplate.convertAndSend("/topic/chat/" + messageRequest.getRoomId(), message);
    }
}

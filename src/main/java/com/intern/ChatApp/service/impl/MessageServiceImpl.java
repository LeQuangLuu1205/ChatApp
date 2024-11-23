package com.intern.ChatApp.service.impl;

import com.intern.ChatApp.dto.request.MessageRequest;
import com.intern.ChatApp.dto.response.MessageResponse;
import com.intern.ChatApp.dto.response.UserResponse;
import com.intern.ChatApp.entity.Message;
import com.intern.ChatApp.entity.Room;
import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.enums.ErrorCode;
import com.intern.ChatApp.exception.AppException;
import com.intern.ChatApp.repository.MessageRepository;
import com.intern.ChatApp.repository.RoomRepository;
import com.intern.ChatApp.repository.UserRepository;
import com.intern.ChatApp.service.MessageService;
import com.intern.ChatApp.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private KafkaTemplate<String, MessageRequest> kafkaTemplate;
    private final String kafkaTopic = "chat-messages";
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private SecurityUtil securityUtil;
    @Override
    public void sendMessage(MessageRequest messageRequest, Integer roomId) {
        kafkaTemplate.send("chat-room-" + roomId, messageRequest);
    }

    @Override
    public MessageResponse saveMessageToDatabase(MessageRequest messageRequest, Integer roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        User user = userRepository.findByEmail(messageRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = messageRepository.save(Message.builder()
                .messageText(messageRequest.getMessageText())
                .fileUrl(messageRequest.getFileUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .room(room)
                .isDisabled(false)
                .user(user)
                .build());
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .imagePath(user.getImagePath())
                                    .build();
        MessageResponse response = MessageResponse.builder()
                .id(message.getId())
                .messageText(message.getMessageText())
                .fileUrl(message.getFileUrl())
                .isPinned(message.getIsPinned())
                .createdAt(message.getCreatedAt())
                .userResponse(userResponse)
                        .build();



        return  response;
    }

    @Override
    @Transactional
    public void disableMessage(Integer messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        boolean currentState = Boolean.TRUE.equals(message.getIsDisabled());
        message.setIsDisabled(!currentState);

        message.setUpdatedAt(LocalDateTime.now());

        messageRepository.save(message);
    }

    @Override
    @Transactional
    public MessageResponse pinMessage(Integer messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        if (Boolean.TRUE.equals(message.getIsDisabled())) {
            throw new AppException(ErrorCode.MESSAGE_DISABLED);
        }

        Room room = message.getRoom();

        messageRepository.unpinMessagesInRoom(room.getId());

        message.setIsPinned(true);
        message.setUpdatedAt(LocalDateTime.now());
        messageRepository.save(message);

        return MessageResponse.builder()
                .id(message.getId())
                .messageText(message.getMessageText())
                .fileUrl(message.getFileUrl())
                .isPinned(message.getIsPinned())
                .createdAt(message.getCreatedAt())
                .build();
    }
}

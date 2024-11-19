package com.intern.ChatApp.dto.request;

import lombok.Data;

@Data
public class AddUserToRoomRequest {
    private Integer roomId;
    private String email;
}

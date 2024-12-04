package com.intern.ChatApp.dto.request;

import lombok.Data;

@Data
public class RemoveUserFromRoomRequest {
    private Integer roomId;
    private Integer userId;
}

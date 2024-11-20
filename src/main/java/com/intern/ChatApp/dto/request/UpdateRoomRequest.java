package com.intern.ChatApp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateRoomRequest {
    @NotBlank(message = "Room name must not be blank")
    @Size(max = 100, message = "Room name must be less than 100 characters")
    private String name;
}

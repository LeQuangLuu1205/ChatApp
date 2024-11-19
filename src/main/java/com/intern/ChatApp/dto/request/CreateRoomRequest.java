package com.intern.ChatApp.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateRoomRequest {
    private String name;
    private List<String> memberEmails;
}

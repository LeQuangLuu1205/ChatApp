package com.intern.ChatApp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {

    @JsonProperty("messageText")
    private String messageText;
    @JsonProperty("fileUrl")
    private String fileUrl;
    @JsonProperty("roomId")
    private Integer roomId;
    @JsonProperty("email")
    private String email;

    private String imageBase64;
}
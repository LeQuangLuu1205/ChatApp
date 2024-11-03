package com.intern.ChatApp.entity.keys;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class RoomUserId implements Serializable {
    private Integer roomId;
    private Integer userId;
}
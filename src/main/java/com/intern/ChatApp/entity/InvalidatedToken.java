package com.intern.ChatApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InvalidatedToken {

    @Id
    private String id;
    private Date expiryTime;
}

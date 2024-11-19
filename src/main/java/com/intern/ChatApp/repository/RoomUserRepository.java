package com.intern.ChatApp.repository;

import com.intern.ChatApp.entity.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomUserRepository extends JpaRepository<RoomUser, Integer> {
}

package com.intern.ChatApp.repository;

import com.intern.ChatApp.entity.Room;
import com.intern.ChatApp.entity.RoomUser;
import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.entity.keys.RoomUserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomUserRepository extends JpaRepository<RoomUser, RoomUserId> {
    Optional<RoomUser> findByRoomAndUser(Room room, User user);
}

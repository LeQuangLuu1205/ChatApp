package com.intern.ChatApp.repository;

import com.intern.ChatApp.entity.Role;
import com.intern.ChatApp.entity.Room;
import com.intern.ChatApp.entity.RoomUser;
import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.entity.keys.RoomUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomUserRepository extends JpaRepository<RoomUser, RoomUserId> {
    Optional<RoomUser> findByRoomAndUser(Room room, User user);
    @Query("SELECT CASE WHEN COUNT(ru) > 0 THEN TRUE ELSE FALSE END " +
            "FROM RoomUser ru " +
            "WHERE ru.room.id = :roomId AND ru.user.email = :email")
    boolean existsByRoomIdAndUserEmail(@Param("roomId") Integer roomId, @Param("email") String email);

    @Query("SELECT ru FROM RoomUser ru WHERE ru.user.email = :email")
    List<RoomUser> findByUserEmail(@Param("email") String email);
    List<RoomUser> findByRoom(Room room);
}

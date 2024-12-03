package com.intern.ChatApp.repository;

import com.intern.ChatApp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByRoomId(Integer roomId);
    List<Message> findByRoom_Id(Integer roomId);
    @Modifying
    @Query("UPDATE Message m SET m.isPinned = false WHERE m.room.id = :roomId AND m.isPinned = true")
    void unpinMessagesInRoom(@Param("roomId") Integer roomId);

    List<Message> findAllByRoom_IdAndIsDisabledFalseOrderByCreatedAtAsc(Integer roomId);
}

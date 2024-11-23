package com.intern.ChatApp.repository;

import com.intern.ChatApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByIsDisabledFalse();
    @Query("SELECT r FROM Room r WHERE r.createdBy.email = :email")
    List<Room> findByCreatedByEmail(@Param("email") String email);
}

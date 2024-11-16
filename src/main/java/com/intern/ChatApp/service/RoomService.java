package com.intern.ChatApp.service;

import com.intern.ChatApp.entity.Room;

import java.util.List;

public interface RoomService {
    void addRoom(Room room);

    List<Room> getRooms();

    Room getRoom(Integer id);

    void updateRoom(Integer id, Room room);

    void deleteRoom(Integer id);
}

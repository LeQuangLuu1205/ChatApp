package com.intern.ChatApp.service;

import com.intern.ChatApp.dto.request.AddUserToRoomRequest;
import com.intern.ChatApp.dto.request.CreateRoomRequest;
import com.intern.ChatApp.dto.response.RoomResponse;
import com.intern.ChatApp.entity.Room;

import java.util.List;

public interface RoomService {
    void addRoom(Room room);
    List<Room> getRooms();
    Room getRoom(Integer id);
    void updateRoom(Integer id, Room room);
    void deleteRoom(Integer id);
    RoomResponse createRoomWithMembers(CreateRoomRequest request);
    RoomResponse addUserToRoom(AddUserToRoomRequest request);
}

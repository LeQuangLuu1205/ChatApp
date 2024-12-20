package com.intern.ChatApp.service;

import com.intern.ChatApp.dto.request.AddUserToRoomRequest;
import com.intern.ChatApp.dto.request.CreateRoomRequest;
import com.intern.ChatApp.dto.request.RemoveUserFromRoomRequest;
import com.intern.ChatApp.dto.request.UpdateRoomRequest;
import com.intern.ChatApp.dto.response.RoomResponse;
import com.intern.ChatApp.dto.response.UserResponse;
import com.intern.ChatApp.entity.Room;

import java.util.List;

public interface RoomService {
    void addRoom(Room room);
    Room getRoom(Integer id);
    void updateRoom(Integer id, Room room);
    void deleteRoom(Integer id);
    RoomResponse createRoomWithMembers(CreateRoomRequest request);
    RoomResponse addUserToRoom(AddUserToRoomRequest request);
    RoomResponse removeUserFromRoom(RemoveUserFromRoomRequest request);
    RoomResponse updateRoomName(Integer roomId, UpdateRoomRequest request);
    void disableRoom(Integer roomId);
    List<RoomResponse> getAllRooms();
    List<RoomResponse> getUserRooms();
    List<RoomResponse> getCreatedRooms();
    RoomResponse getRoomById(Integer groupId);
    List<UserResponse> getUsersInRoom(Integer roomId);
}

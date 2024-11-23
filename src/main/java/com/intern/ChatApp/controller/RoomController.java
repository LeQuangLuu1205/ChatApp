package com.intern.ChatApp.controller;

import com.intern.ChatApp.dto.request.AddUserToRoomRequest;
import com.intern.ChatApp.dto.request.CreateRoomRequest;
import com.intern.ChatApp.dto.request.RemoveUserFromRoomRequest;
import com.intern.ChatApp.dto.request.UpdateRoomRequest;
import com.intern.ChatApp.dto.response.ApiResponse;
import com.intern.ChatApp.dto.response.RoomResponse;
import com.intern.ChatApp.entity.Room;
import com.intern.ChatApp.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private SecurityUtil securityUtil;
    @PostMapping
    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
    public ResponseEntity<ApiResponse<RoomResponse>> createRoomWithMembers(@RequestBody CreateRoomRequest request) {
        RoomResponse room = roomService.createRoomWithMembers(request);
        return ResponseEntity.ok(
                ApiResponse.<RoomResponse>builder()
                        .result(room)
                        .message("Room created successfully with members")
                        .build()
        );
    }

    @PostMapping("/add-user")
    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
    public ResponseEntity<ApiResponse<RoomResponse>> addUserToRoom(@RequestBody AddUserToRoomRequest request) {
        RoomResponse roomResponse = roomService.addUserToRoom(request);
        return ResponseEntity.ok(ApiResponse.<RoomResponse>builder()
                .result(roomResponse)
                .message("User added to room successfully")
                .build());
    }

    @PostMapping("/remove-user")
    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
    public ResponseEntity<ApiResponse<RoomResponse>> removeUserFromRoom(@RequestBody RemoveUserFromRoomRequest request) {
        RoomResponse roomResponse = roomService.removeUserFromRoom(request);
        return ResponseEntity.ok(ApiResponse.<RoomResponse>builder()
                .result(roomResponse)
                .message("User removed from room successfully")
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getAllRooms() {
        List<RoomResponse> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(
                ApiResponse.<List<RoomResponse>>builder()
                        .code(1000)
                        .message("Rooms retrieved successfully")
                        .result(rooms)
                        .build()
        );
    }

    @PostMapping("/add")
    public String addRoom(@RequestBody Room room){
        roomService.addRoom(room);

        return "Success add room";
    }



    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getRoomById(@RequestParam Integer id){
        Room room = roomService.getRoom(id);

        return ApiResponse.builder()
                .result(room).build();
    }

    @PutMapping("/{roomId}")
    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
    public ResponseEntity<ApiResponse<RoomResponse>> updateRoomName(@PathVariable Integer roomId,
                             @RequestBody UpdateRoomRequest request){

        RoomResponse roomResponse = roomService.updateRoomName(roomId, request);

        return ResponseEntity.ok(ApiResponse.<RoomResponse>builder()
                .result(roomResponse)
                .message("Room updated successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Integer id) {
        roomService.disableRoom(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(1000)
                        .message("Room disabled successfully")
                        .build()
        );
    }

    @GetMapping("/user-rooms")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getUserRooms() {
        List<RoomResponse> rooms = roomService.getUserRooms();
        return ResponseEntity.ok(
                ApiResponse.<List<RoomResponse>>builder()
                        .code(1000)
                        .message("User rooms retrieved successfully")
                        .result(rooms)
                        .build()
        );
    }

    @GetMapping("/created-rooms")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getCreatedRooms() {
        List<RoomResponse> rooms = roomService.getCreatedRooms();
        return ResponseEntity.ok(
                ApiResponse.<List<RoomResponse>>builder()
                        .code(1000)
                        .message("Rooms created by user retrieved successfully")
                        .result(rooms)
                        .build()
        );
    }
}

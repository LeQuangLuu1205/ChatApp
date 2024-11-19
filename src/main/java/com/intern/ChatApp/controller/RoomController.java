package com.intern.ChatApp.controller;

import com.intern.ChatApp.dto.request.CreateRoomRequest;
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
    public ResponseEntity<ApiResponse<RoomResponse>> createRoomWithMembers(@RequestBody CreateRoomRequest request) {
        RoomResponse room = roomService.createRoomWithMembers(request);
        return ResponseEntity.ok(
                ApiResponse.<RoomResponse>builder()
                        .result(room)
                        .message("Room created successfully with members")
                        .build()
        );
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('MODERATOR')")
    public ApiResponse<?> getAllRoom() {
        String email = securityUtil.extractEmailFromSecurityContext();
        return ApiResponse.<String>builder()
                .result(email)
                .message("Email retrieved successfully")
                .build();
    }

    @PostMapping("/add")
    //@PreAuthorize("hasRole('ADMIN')")
    public String addRoom(@RequestBody Room room){
        roomService.addRoom(room);

        return "Success add room";
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getAllRooms(){
        List<Room> rooms = roomService.getRooms();
        return ApiResponse.builder()
                .result(rooms)
                .build();
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getRoomById(@RequestParam Integer id){
        Room room = roomService.getRoom(id);

        return ApiResponse.builder()
                .result(room).build();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateRoom(@PathVariable Integer id, @RequestBody Room room){
        roomService.updateRoom(id, room);

        return "Success upadate room";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteRoom(@PathVariable Integer id){
        roomService.deleteRoom(id);

        return "Success delete room";
    }
}

package com.intern.ChatApp.controller;

import com.intern.ChatApp.dto.response.ApiResponse;
import com.intern.ChatApp.entity.Room;
import com.intern.ChatApp.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('MODERATOR')")
    public ApiResponse<?> getAllRoom() {
        return ApiResponse.<String>builder()
                .result("Hello")
                .build();
    }
    @GetMapping("/getdemo")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getDemo() {
        return ApiResponse.<String>builder()
                .result("Hello")
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

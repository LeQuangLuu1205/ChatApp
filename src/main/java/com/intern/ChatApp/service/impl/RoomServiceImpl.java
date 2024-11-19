package com.intern.ChatApp.service.impl;

import com.intern.ChatApp.dto.request.CreateRoomRequest;
import com.intern.ChatApp.dto.response.RoomResponse;
import com.intern.ChatApp.entity.Room;
import com.intern.ChatApp.entity.RoomUser;
import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.entity.keys.RoomUserId;
import com.intern.ChatApp.enums.ErrorCode;
import com.intern.ChatApp.exception.AppException;
import com.intern.ChatApp.repository.RoomRepository;
import com.intern.ChatApp.repository.RoomUserRepository;
import com.intern.ChatApp.repository.UserRepository;
import com.intern.ChatApp.service.RoomService;
import com.intern.ChatApp.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomUserRepository roomUserRepository;

    @Override
    public void addRoom(Room room) {
        roomRepository.save(room);
    }

    @Override
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoom(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid room id" + id));
        return room;
    }

    @Override
    public void updateRoom(Integer id, Room room) {
        roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid room id" + id));
        room.setId(id);

        roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid room id " + id));
        roomRepository.delete(room);
    }


    @Override
    @Transactional
    public RoomResponse createRoomWithMembers(CreateRoomRequest request) {

        String mail = securityUtil.extractEmailFromSecurityContext();
        User createBy = userRepository.findByEmail(mail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        validateRoomRequest(request);

        Room room = createRoom(request, createBy);

        List<RoomUser> roomUsers = createRoomUsers(request.getMemberEmails(), room, createBy);

        saveRoomUsers(roomUsers);

        return buildRoomResponse(room);
    }

    // Phương thức kiểm tra đầu vào
    private void validateRoomRequest(CreateRoomRequest request) {

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        if (request.getMemberEmails() == null || request.getMemberEmails().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
    }

    // Phương thức tạo phòng
    private Room createRoom(CreateRoomRequest request, User createBy) {
        Room room = new Room();
        room.setName(request.getName());
        room.setCreatedBy(createBy);
        return roomRepository.save(room);
    }

    private List<RoomUser> createRoomUsers(List<String> memberEmails, Room room, User createBy) {
        List<RoomUser> roomUsers = memberEmails.stream()
                .map(email -> {
                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                    RoomUser roomUser = new RoomUser();
                    roomUser.setRoom(room);
                    roomUser.setUser(user);
                    roomUser.setId(new RoomUserId(room.getId(), user.getId()));

                    return roomUser;
                })
                .collect(Collectors.toList());

        RoomUser creatorRoomUser = new RoomUser();
        creatorRoomUser.setRoom(room);
        creatorRoomUser.setUser(createBy);
        creatorRoomUser.setId(new RoomUserId(room.getId(), createBy.getId()));
        roomUsers.add(creatorRoomUser);

        return roomUsers;
    }


    private void saveRoomUsers(List<RoomUser> roomUsers) {
        try {
            roomUserRepository.saveAll(roomUsers);
        } catch (Exception e) {
            throw new AppException(ErrorCode.DATABASE_ERROR);
        }
    }

    // Phương thức xây dựng response
    private RoomResponse buildRoomResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .createdByEmail(room.getCreatedBy().getEmail())
                .build();
    }
}

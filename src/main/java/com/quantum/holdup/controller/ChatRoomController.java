package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.ChatRoomDTO;
import com.quantum.holdup.domain.entity.ChatRoom;
import com.quantum.holdup.service.ChatRoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("holdup")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    // 모든 채팅방 조회 (GET)
    @GetMapping("/rooms")
    public List<ChatRoomDTO> getRooms() {
        return chatRoomService.findAllRooms();
    }

    // 특정 채팅방 조회 (GET) - URL을 명확하게 /room/{roomId}로 변경
    @GetMapping("/room/{roomId}")
    public ChatRoom getRoom(@PathVariable Long roomId) {
        return chatRoomService.findById(roomId);
    }

    // 채팅방 생성 (POST)
    @PostMapping("/room")
    public ChatRoomDTO createRoom(@RequestBody String name) {
        return chatRoomService.createRoom(name);
    }

    // 채팅방 삭제 (DELETE)
    @DeleteMapping("/room/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        chatRoomService.deleteRoom(roomId);
    }
}
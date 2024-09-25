package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.ChatRoomDTO;
import com.quantum.holdup.service.ChatRoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping("/rooms")
    public List<ChatRoomDTO> getRooms() {
        return chatRoomService.findAll();
    }

    @PostMapping("/room")
    public ChatRoomDTO createRoom(@RequestBody String name) {
        return chatRoomService.createRoom(name);
    }
}
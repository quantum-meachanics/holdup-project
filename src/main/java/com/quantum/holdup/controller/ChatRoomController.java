package com.quantum.holdup.controller;

import com.ohgiraffers.chat.dto.ChatRoomDTO;
import com.ohgiraffers.chat.service.ChatRoomService;
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
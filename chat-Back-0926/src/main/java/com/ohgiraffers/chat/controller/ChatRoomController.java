package com.ohgiraffers.chat.controller;

import com.ohgiraffers.chat.dto.ChatRoomDTO;
import com.ohgiraffers.chat.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping
    public ResponseEntity<ChatRoomDTO> createChatRoom(@RequestBody ChatRoomDTO chatRoomDTO) {
        return ResponseEntity.ok(chatRoomService.createChatRoom(chatRoomDTO));
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomDTO>> getAllChatRooms() {
        return ResponseEntity.ok(chatRoomService.getAllChatRooms());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> leaveChatRoom(@PathVariable Long id) {
        chatRoomService.leaveChatRoom(id);
        return ResponseEntity.ok().build();
    }
}
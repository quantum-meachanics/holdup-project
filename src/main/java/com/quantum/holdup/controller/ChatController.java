package com.quantum.holdup.controller;

import com.quantum.holdup.domain.entity.ChatRoom;
import com.quantum.holdup.domain.entity.Message;
import com.quantum.holdup.repository.MessageRepository;
import com.quantum.holdup.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("holdup")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService, MessageRepository messageRepository) {
        this.chatService = chatService;
    }

    @PostMapping("/rooms")
    public ChatRoom createChatRoom(@RequestBody String name) {
        return chatService.createChatRoom(name);
    }

    @GetMapping("/rooms")
    public List<ChatRoom> getAllChatRooms() {
        return chatService.getAllChatRooms();
    }

    // 채팅방의 메시지 기록불러오기
    @GetMapping("/rooms/{id}/messages")
    public List<com.quantum.holdup.domain.entity.Message> getMessages(@PathVariable Long id) {
        return chatService.getMessagesByChatRoom(id);
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(Message chatMessage) {
        // 메시지 저장 로직 추가
        chatService.saveMessage(chatMessage.getSender(), chatMessage.getContent(), "public");
        return chatMessage;  // 클라이언트에게 전송된 메시지를 다시 브로드캐스트
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(Message chatMessage) {
        return chatMessage;  // 유저 추가 로직
    }
}
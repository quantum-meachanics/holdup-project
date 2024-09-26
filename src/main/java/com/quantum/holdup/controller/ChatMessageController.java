package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.ChatMessageDTO;
import com.quantum.holdup.service.ChatMessageService;
import com.quantum.holdup.service.ChatRoomService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    private final ChatRoomService chatRoomService;

    public ChatMessageController(ChatMessageService chatMessageService, ChatRoomService chatRoomService) {
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
    }

    // WebSocket으로 메시지 전송 시 호출되는 메서드
    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessageDTO sendMessage(@RequestBody ChatMessageDTO messageDTO) {
        // ChatMessageService의 saveMessage 메서드를 호출하여 메시지를 저장
        return chatMessageService.saveMessage(messageDTO);
    }

    // 특정 채팅방의 메시지 조회
    @GetMapping("/messages/{roomId}")
    public List<ChatMessageDTO> getMessages(@PathVariable Long roomId) {
        // ChatRoomService에서 getMessagesByRoomId를 호출하여 메시지 목록을 가져옴
        return chatRoomService.getMessagesByRoomId(roomId);
    }
}

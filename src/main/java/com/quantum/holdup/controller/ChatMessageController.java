package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.ChatMessageDTO;
import com.quantum.holdup.domain.entity.ChatMessage;
import com.quantum.holdup.service.ChatMessageService;
import com.quantum.holdup.service.ChatRoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService, ChatRoomService chatRoomService) {
        this.chatMessageService = chatMessageService;
    }

    // WebSocket으로 메시지 전송 시 호출되는 메서드
    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessageDTO sendMessage(@DestinationVariable Long roomId, @RequestBody ChatMessageDTO messageDTO) {
        // ChatMessageService의 saveMessage 메서드를 호출하여 메시지 저장
        return chatMessageService.saveMessage(messageDTO);
    }

    // 특정 채팅방의 메시지를 조회하는 API
    @GetMapping("/messages/{roomId}")
    public List<ChatMessageDTO> getMessagesByRoomId(@PathVariable Long roomId) {
        // ChatMessageService에서 getMessagesBy 호출
        return chatMessageService.getMessagesBy(roomId);
    }
}

package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.ChatMessageDTO;
import com.quantum.holdup.domain.entity.ChatMessage;
import com.quantum.holdup.service.ChatMessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/{roomId}")
    public ChatMessageDTO sendMessage(@DestinationVariable Long roomId, ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(chatMessageDTO.getSender());
        chatMessage.setContent(chatMessageDTO.getContent());
        chatMessage.setTimestamp(LocalDateTime.now());
        // 방 정보를 설정해야 하므로 ChatRoomRepository를 통해 방 객체를 가져온 후 설정
        // chatMessage.setChatRoom(chatRoomRepository.findById(roomId).get());
        return chatMessageService.saveMessage(chatMessage);
    }

    @GetMapping("/messages/{roomId}")
    public List<ChatMessageDTO> getMessages(@PathVariable Long roomId) {
        return chatMessageService.getMessages(roomId);
    }
}
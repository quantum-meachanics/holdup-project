package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.ChatMessageDTO;
import com.quantum.holdup.domain.entity.ChatMessage;
import com.quantum.holdup.domain.entity.ChatRoom;
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

    private final ChatRoomService chatRoomService;

    public ChatMessageController(ChatMessageService chatMessageService, ChatRoomService chatRoomService) {
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
    }

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessageDTO sendMessage(@DestinationVariable Long roomId, @RequestBody ChatMessageDTO messageDTO) {
        // ChatRoomService의 findById 호출하여 채팅방을 가져옴
        ChatRoom chatRoom = chatRoomService.findById(roomId);

        ChatMessage message = new ChatMessage();
        message.setSender(messageDTO.getSender());
        message.setContent(messageDTO.getContent());
        message.setChatRoom(chatRoom);

        return chatMessageService.saveMessage(message);
    }

    @GetMapping("/messages/{roomId}")
    public List<ChatMessageDTO> getMessages(@PathVariable Long roomId) {
        return chatMessageService.getMessages(roomId);
    }
}
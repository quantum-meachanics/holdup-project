package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.ChatMessageDTO;
import com.quantum.holdup.domain.entity.ChatMessage;
import com.quantum.holdup.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<ChatMessageDTO> getMessagesBy(Long roomId) {
        return chatMessageRepository.findByChatRoomId(roomId).stream()
                .map(msg -> {
                    ChatMessageDTO dto = new ChatMessageDTO();
                    dto.setId(msg.getId());
                    dto.setSender(msg.getSender());
                    dto.setContent(msg.getContent());
                    dto.setRoomId(roomId);
                    dto.setTimestamp(msg.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ChatMessageDTO saveMessage(ChatMessage chatMessage) {
        chatMessage = chatMessageRepository.save(chatMessage);
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(chatMessage.getId());
        dto.setSender(chatMessage.getSender());
        dto.setContent(chatMessage.getContent());
        dto.setRoomId(chatMessage.getChatRoom().getId());
        dto.setTimestamp(chatMessage.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return dto;
    }
}
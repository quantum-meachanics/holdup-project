package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.ChatMessageDTO;
import com.quantum.holdup.domain.entity.ChatMessage;
import com.quantum.holdup.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public List<ChatMessageDTO> getMessages(Long roomId) {
        return chatMessageRepository.findByChatRoomId(roomId).stream()
                .map(message -> new ChatMessageDTO(
                        message.getId(),
                        message.getSender(),
                        message.getContent(),
                        message.getTimestamp()
                ))
                .collect(Collectors.toList());
    }

    public ChatMessageDTO saveMessage(ChatMessage chatMessage) {
        chatMessage = chatMessageRepository.save(chatMessage);
        return new ChatMessageDTO(
                chatMessage.getId(),
                chatMessage.getSender(),
                chatMessage.getContent(),
                chatMessage.getTimestamp()
        );
    }
}
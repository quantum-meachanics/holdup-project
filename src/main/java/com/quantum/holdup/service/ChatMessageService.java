package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.ChatMessageDTO;
import com.quantum.holdup.domain.entity.ChatMessage;
import com.quantum.holdup.domain.entity.ChatRoom;
import com.quantum.holdup.repository.ChatMessageRepository;
import com.quantum.holdup.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }


    // 특정 채팅방의 메시지를 조회하는 메서드
    public List<ChatMessageDTO> getMessagesByRoomId(Long roomId) {
        return chatMessageRepository.findByChatRoomId(roomId).stream()
                .map(msg -> {
                    ChatMessageDTO dto = new ChatMessageDTO();
                    dto.setId(msg.getId());
                    dto.setSender(msg.getSender());
                    dto.setContent(msg.getContent());
                    dto.setRoomId(roomId);
                    dto.setTimestamp(msg.getTimestamp().toString());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ChatMessageDTO saveMessage(ChatMessageDTO messageDTO) {
        // ChatRoom을 DB에서 조회
        ChatRoom chatRoom = chatRoomRepository.findById(messageDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID: " + messageDTO.getRoomId()));

        // 새로운 ChatMessage 객체 생성 및 저장
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(messageDTO.getSender());
        chatMessage.setContent(messageDTO.getContent());
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setTimestamp(LocalDateTime.now());

        // 메시지를 저장하고 DTO로 반환
        chatMessage = chatMessageRepository.save(chatMessage);
        messageDTO.setId(chatMessage.getId());
        messageDTO.setTimestamp(chatMessage.getTimestamp().toString());
        return messageDTO;
    }
}
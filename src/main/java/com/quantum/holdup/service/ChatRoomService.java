package com.quantum.holdup.service;


import com.quantum.holdup.domain.dto.ChatMessageDTO;
import com.quantum.holdup.domain.dto.ChatRoomDTO;
import com.quantum.holdup.domain.entity.ChatMessage;
import com.quantum.holdup.domain.entity.ChatRoom;
import com.quantum.holdup.repository.ChatMessageRepository;
import com.quantum.holdup.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    // 채팅방 목록 조회
    public List<ChatRoomDTO> findAllRooms() {
        return chatRoomRepository.findAll().stream()
                .map(room -> {
                    ChatRoomDTO dto = new ChatRoomDTO();
                    dto.setId(room.getId());
                    dto.setName(room.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 채팅방 생성
    public ChatRoomDTO createRoom(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        chatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomDTO dto = new ChatRoomDTO();
        dto.setId(chatRoom.getId());
        dto.setName(chatRoom.getName());
        return dto;
    }

    // 채팅방 삭제
    public void deleteRoom(Long roomId) {
        chatRoomRepository.deleteById(roomId);
    }

    public ChatRoom findById(Long roomId) {
        // Optional로 반환된 결과를 처리합니다.
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID: " + roomId));
    }
}

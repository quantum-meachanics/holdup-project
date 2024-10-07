package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.ChatRoomDTO;
import com.quantum.holdup.domain.entity.ChatRoom;
import com.quantum.holdup.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoomDTO createChatRoom(ChatRoomDTO chatRoomDTO) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(chatRoomDTO.getName());
        chatRoom = chatRoomRepository.save(chatRoom);

        chatRoomDTO.setId(chatRoom.getId());
        return chatRoomDTO;
    }

    public List<ChatRoomDTO> getAllChatRooms() {
        return chatRoomRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ChatRoomDTO convertToDTO(ChatRoom chatRoom) {
        ChatRoomDTO dto = new ChatRoomDTO();
        dto.setId(chatRoom.getId());
        dto.setName(chatRoom.getName());
        return dto;
    }
}
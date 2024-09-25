package com.quantum.holdup.service;

import com.ohgiraffers.chat.dto.ChatRoomDTO;
import com.ohgiraffers.chat.entity.ChatRoom;
import com.ohgiraffers.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<ChatRoomDTO> findAll() {
        return chatRoomRepository.findAll().stream()
                .map(room -> new ChatRoomDTO(room.getId(), room.getName()))
                .collect(Collectors.toList());
    }

    public ChatRoomDTO createRoom(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        chatRoom = chatRoomRepository.save(chatRoom);
        return new ChatRoomDTO(chatRoom.getId(), chatRoom.getName());
    }
}
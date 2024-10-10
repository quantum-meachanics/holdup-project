package com.ohgiraffers.chat.service;

import com.ohgiraffers.chat.entity.ChatRoom;
import com.ohgiraffers.chat.entity.Message;
import com.ohgiraffers.chat.repository.ChatRoomRepository;
import com.ohgiraffers.chat.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, MessageRepository messageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
    }

    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    // 메시지 저장
    public void saveMessage(String sender, String content, String roomId) {
        Message message = new Message();
        message.setSender(sender);
        message.setContent(content);
        message.setRoomId(roomId);
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
    }

    // 특정 채팅방의 메시지 기록 가져오기
    public List<Message> getMessagesByChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
        return chatRoom.getMessages();
    }

}
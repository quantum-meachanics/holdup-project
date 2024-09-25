package com.quantum.holdup.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatMessageDTO {
    private Long id;
    private String sender;
    private String content;
    private LocalDateTime timestamp;

    // 생성자, getter, setter
    public ChatMessageDTO() {}

    public ChatMessageDTO(Long id, String sender, String content, LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

}
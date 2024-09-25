package com.quantum.holdup.domain.dto;

import java.time.LocalDateTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
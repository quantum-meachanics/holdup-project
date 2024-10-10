package com.ohgiraffers.chat.dto;

import lombok.*;

import java.awt.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private TrayIcon.MessageType type;
    private String content;
    private String sender;
}
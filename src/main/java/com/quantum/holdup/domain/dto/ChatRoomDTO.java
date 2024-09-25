package com.quantum.holdup.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatRoomDTO {
    private Long id;
    private String name;

    // 생성자, getter, setter
    public ChatRoomDTO() {}

    public ChatRoomDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
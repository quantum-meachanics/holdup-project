package com.quantum.holdup.domain.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailRequestDTO {

    private String email; // 회원 이메일

    @Builder
    public EmailRequestDTO(String email) {
        this.email = email;
    }
}

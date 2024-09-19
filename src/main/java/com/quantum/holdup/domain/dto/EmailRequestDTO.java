package com.quantum.holdup.domain.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailRequestDTO {

    private String email; // 회원 이메일
    private String nickname; // 회원 닉네임

    @Builder
    public EmailRequestDTO(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}

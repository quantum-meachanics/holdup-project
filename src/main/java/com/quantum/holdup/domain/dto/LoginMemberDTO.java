package com.quantum.holdup.domain.dto;

import lombok.*;

@NoArgsConstructor
@Getter

public class LoginMemberDTO {

    private String email; // 회원 이메일
    private String password; // 회원 비밀번호

    @Builder
    public LoginMemberDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

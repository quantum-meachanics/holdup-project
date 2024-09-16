package com.quantum.holdup.domain.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PasswordResetRequestDTO {

    private String nickname;
    private String email;
    private String code;
    private String newPassword;

    @Builder
    public PasswordResetRequestDTO(String nickname, String email, String code, String newPassword) {
        this.nickname = nickname;
        this.email = email;
        this.code = code;
        this.newPassword = newPassword;
    }
}

package com.quantum.holdup.domain.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VerificationRequestDTO {

    private String email;
    private String verificationCode;
    private String newPassword;

    @Builder
    public VerificationRequestDTO(String email,String verificationCode,String newPassword) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.newPassword = newPassword;
    }
}

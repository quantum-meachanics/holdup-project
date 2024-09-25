package com.quantum.holdup.domain.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VerificationDTO {

    private String email;
    private String verificationCode;

    @Builder
    public VerificationDTO(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }
}

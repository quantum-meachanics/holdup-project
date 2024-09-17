package com.quantum.holdup.domain.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VerificationRequestDTO {

    private String email;


    @Builder
    public VerificationRequestDTO(String email) {
        this.email = email;
    }
}

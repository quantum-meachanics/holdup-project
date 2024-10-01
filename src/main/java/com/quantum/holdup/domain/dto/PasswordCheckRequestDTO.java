package com.quantum.holdup.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PasswordCheckRequestDTO {
    private String currentPassword;
    private String email;

    @Builder
    public PasswordCheckRequestDTO(String currentPassword, String email) {
        this.currentPassword = currentPassword;
        this.email = email;
    }
}

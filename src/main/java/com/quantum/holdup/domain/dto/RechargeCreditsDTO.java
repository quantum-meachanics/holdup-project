package com.quantum.holdup.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RechargeCreditsDTO {

    private int amount;
    private String email;

    @Builder
    public RechargeCreditsDTO(int amount, String email) {
        this.amount = amount;
        this.email = email;
    }
}

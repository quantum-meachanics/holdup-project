package com.quantum.holdup.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String code;
    private LocalDateTime sentAt;

    @Builder
    public VerificationCode(Long id, String email, String code, LocalDateTime sentAt) {
        this.id = id;
        this.email = email;
        this.code = code;
        this.sentAt = sentAt;
    }
}

package com.quantum.holdup.repository;

import com.quantum.holdup.domain.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByEmail(String email);

    List<VerificationCode> findLatestByEmail(String email);
}
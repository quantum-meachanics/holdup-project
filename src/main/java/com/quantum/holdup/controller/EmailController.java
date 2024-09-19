package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.EmailRequestDTO;
import com.quantum.holdup.domain.dto.VerificationRequestDTO;
import com.quantum.holdup.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("holdup")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody EmailRequestDTO emailRequestDTO) {
        try {
            emailService.sendVerificationCode(emailRequestDTO);
            return ResponseEntity.ok("Verification code sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Email sending failed: " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send verification code.");
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody VerificationRequestDTO verificationRequest) {
        boolean isVerified = emailService.verifyCode(verificationRequest);
        return isVerified
                ? ResponseEntity.ok("Verification successful.")
                : ResponseEntity.status(400).body("Invalid or expired verification code.");
    }

}

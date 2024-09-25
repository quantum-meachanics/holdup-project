package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.EmailRequestDTO;
import com.quantum.holdup.domain.dto.VerificationDTO;
import com.quantum.holdup.domain.dto.VerificationRequestDTO;
import com.quantum.holdup.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/signup-send-verification-code")
    public ResponseEntity<String> signupSendVerificationCode(@RequestBody EmailRequestDTO emailRequestDTO) {
        try {
            // 이메일 유효성 검사 (필요에 따라 추가)
            if (!isValidEmail(emailRequestDTO.getEmail())) {
                return ResponseEntity.badRequest().body("유효하지 않은 이메일입니다.");
            }

            emailService.sendSignupVerificationCode(emailRequestDTO);
            return ResponseEntity.ok("Signup verification code sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace(); // 로그에 기록
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이메일 전송 실패: 서버에 문의하세요.");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace(); // 로그에 기록
            return ResponseEntity.badRequest().body("유효하지 않은 데이터입니다.");
        } catch (Exception e) {
            e.printStackTrace(); // 로그에 기록
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류가 발생했습니다.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }


    @PostMapping("/signup-verify-code")
    public ResponseEntity<Map<String, Object>> verifySignupCode(@RequestBody VerificationRequestDTO request) {
        boolean isVerified = emailService.signupVerifyCode(request.getEmail(), request.getVerificationCode());

        Map<String, Object> response = new HashMap<>();
        response.put("success", isVerified);
        response.put("message", isVerified ? "인증이 완료되었습니다." : "인증에 실패했습니다.");

        return ResponseEntity.ok(response);
    }
}

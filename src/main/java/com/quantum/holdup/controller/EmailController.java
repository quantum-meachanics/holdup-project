package com.quantum.holdup.controller;

import com.quantum.holdup.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/holdup")
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/send-email")
    public String sendVerificationEmail(@RequestParam String email) {
        try {
            emailService.sendVerificationEmail(email);
            return "Verification email sent successfully.";
        } catch (Exception e) {
            // 로그에 에러를 기록하고, 적절한 오류 메시지를 반환합니다.
            e.printStackTrace();
            return "Failed to send verification email.";
        }
    }

    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam String email, @RequestParam int code) {
        boolean isValid = emailService.verifyCode(email, code);
        if (isValid) {
            return "Verification successful.";
        } else {
            return "Invalid verification code.";
        }
    }

}

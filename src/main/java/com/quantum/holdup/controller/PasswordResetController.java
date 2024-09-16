package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.PasswordResetRequestDTO;
import com.quantum.holdup.domain.dto.VerificationRequestDTO;
import com.quantum.holdup.service.EmailService;
import com.quantum.holdup.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/holdup")
public class PasswordResetController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetService passwordResetService;

    // 이메일로 인증번호 발송 요청
    @PostMapping("/sendVerificationCode")
    public String sendVerificationCode(@RequestBody VerificationRequestDTO request) {
        String email = request.getEmail();
        try {
            emailService.sendVerificationEmail(email);
            return "Verification code sent to " + email;
        } catch (RuntimeException e) {
            return "Failed to send verification code: " + e.getMessage();
        }
    }

    // 비밀번호 변경 요청
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordResetRequestDTO request) {
        String nickname = request.getNickname();
        String email = request.getEmail();
        int code = request.getCode();
        String newPassword = request.getNewPassword();

        boolean isVerified = emailService.verifyCode(email, code);
        if (isVerified) {
            boolean isSuccess = passwordResetService.resetPassword(nickname, email, newPassword, emailService);
            if (isSuccess) {
                return "Password has been successfully reset.";
            } else {
                return "Failed to reset password.";
            }
        } else {
            return "Invalid verification code.";
        }
    }
}
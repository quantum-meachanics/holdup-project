package com.quantum.holdup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private final Map<String, String> emailVerificationCodes = new HashMap<>();

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendVerificationEmail(String to) {
        String verificationCode = generateVerificationCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ggim17861@gmail.com");
        message.setTo(to);
        message.setSubject("Your Verification Code");
        message.setText("Your verification code is: " + verificationCode);

        try {
            javaMailSender.send(message);
            emailVerificationCodes.put(to, verificationCode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = emailVerificationCodes.get(email);
        return storedCode != null && storedCode.equals(code);
    }
}
package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.EmailRequestDTO;
import com.quantum.holdup.domain.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.quantum.holdup.repository.MemberEmailRepository;
import com.quantum.holdup.domain.dto.VerificationRequestDTO;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final MemberEmailRepository memberEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public void sendVerificationCode(EmailRequestDTO emailRequestDTO) throws Exception {

        String verificationCode = generateVerificationCode();
        sendEmail(emailRequestDTO.getEmail(), verificationCode);

        Member member = memberEmailRepository.findByEmail(emailRequestDTO.getEmail());
        if (member == null) {
            throw new RuntimeException("Member with email " + emailRequestDTO.getEmail() + " does not exist.");
        }

        // 기존 사용자가 있으면 인증 코드와 발송 시간 업데이트
        member.setVerificationCode(verificationCode);
        member.setVerificationCodeSentAt(LocalDateTime.now());
        memberEmailRepository.save(member);
    }

    public boolean verifyCode(VerificationRequestDTO verificationRequestDTO) {
        Member member = memberEmailRepository.findByEmail(verificationRequestDTO.getEmail());

        if (member != null) {
            boolean isCodeValid = member.getVerificationCode().equals(verificationRequestDTO.getVerificationCode());
            boolean isCodeExpired = member.getVerificationCodeSentAt().plusMinutes(5).isBefore(LocalDateTime.now());

            if (isCodeValid && !isCodeExpired) {
                member.setPassword(passwordEncoder.encode(verificationRequestDTO.getNewPassword()));
                memberEmailRepository.save(member);
                return true;
            }
        }
        return false;
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 100000에서 999999 사이의 숫자
    }

    private void sendEmail(String recipientEmail, String verificationCode) {
        System.out.println("Sending email to: " + recipientEmail + " with code: " + verificationCode);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ggim17861@gmail.com");
        message.setTo(recipientEmail);
        message.setSubject("Verification Code");
        message.setText("Your verification code is: " + verificationCode);

        try {
            mailSender.send(message);
            System.out.println("Email sent successfully to " + recipientEmail);
        } catch (MailSendException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            // 추가적인 오류 처리 필요
        }
    }

}

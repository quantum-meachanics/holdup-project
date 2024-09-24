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

        // 사용자가 존재하는지 확인
        if (member != null) {
            // 인증 코드가 유효한지 및 만료되지 않았는지 확인
            boolean isCodeValid = member.getVerificationCode().equals(verificationRequestDTO.getVerificationCode());
            boolean isCodeExpired = member.getVerificationCodeSentAt().plusMinutes(5).isBefore(LocalDateTime.now());

            if (isCodeValid && !isCodeExpired) {
                // 인증 코드가 유효하면 비밀번호 변경 로직 추가
                String newPassword = verificationRequestDTO.getNewPassword(); // 새 비밀번호를 DTO 에서 가져옵니다.
                member.setPassword(passwordEncoder.encode(newPassword)); // 비밀번호 설정
                memberEmailRepository.save(member); // 변경 사항 저장
                return true; // 비밀번호 변경 성공
            }
        }
        return false; // 실패
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 100000에서 999999 사이의 숫자
    }

    private void sendEmail(String recipientEmail, String verificationCode) {
        // 이메일 전송 메시지 준비
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ggim17861@gmail.com");
        message.setTo(recipientEmail);
        message.setSubject("Verification Code");
        message.setText("안녕하세요!\n\n" +
                "귀하의 인증 코드는 다음과 같습니다:\n" +
                "✅ " + verificationCode + "\n\n" +
                "이 코드를 사용하여 인증을 완료해 주세요.\n" +
                "감사합니다!");

        // 이메일 전송 시도
        try {
            mailSender.send(message);
            System.out.println("✅ 이메일이 성공적으로 전송되었습니다: " + recipientEmail);
        } catch (MailSendException e) {
            System.err.println("❌ 이메일 전송 실패: " + e.getMessage());
        }
    }

}

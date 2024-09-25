package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.EmailRequestDTO;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.VerificationCode;
import com.quantum.holdup.repository.VerificationCodeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.quantum.holdup.repository.MemberEmailRepository;
import com.quantum.holdup.domain.dto.VerificationRequestDTO;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final MemberEmailRepository memberEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final VerificationCodeRepository verificationCodeRepository;

    // 인증 코드를 임시로 저장하기 위한 맵
    private Map<String, String> verificationCodes = new HashMap<>();

    public void sendVerificationCode(EmailRequestDTO emailRequestDTO) throws Exception {
        String verificationCode = generateVerificationCode();
        sendEmail(emailRequestDTO.getEmail(), verificationCode);

        Member member = memberEmailRepository.findByEmail(emailRequestDTO.getEmail());
        if (member == null) {
            throw new RuntimeException("Member with email " + emailRequestDTO.getEmail() + " does not exist.");
        }

        member.setVerificationCode(verificationCode);
        member.setVerificationCodeSentAt(LocalDateTime.now());
        memberEmailRepository.save(member);
    }

    public void sendSignupVerificationCode(EmailRequestDTO emailRequestDTO) throws Exception {
        String verificationCode = generateVerificationCode();
        sendEmail(emailRequestDTO.getEmail(), verificationCode);

        VerificationCode codeEntity = VerificationCode.builder()
                .email(emailRequestDTO.getEmail())
                .code(verificationCode)
                .sentAt(LocalDateTime.now())
                .build();
        verificationCodeRepository.save(codeEntity);
    }

    public boolean verifyCode(VerificationRequestDTO verificationRequestDTO) {
        Member member = memberEmailRepository.findByEmail(verificationRequestDTO.getEmail());

        if (member != null) {
            boolean isCodeValid = member.getVerificationCode().equals(verificationRequestDTO.getVerificationCode());
            boolean isCodeExpired = member.getVerificationCodeSentAt().plusMinutes(5).isBefore(LocalDateTime.now());

            if (isCodeValid && !isCodeExpired) {
                String newPassword = verificationRequestDTO.getNewPassword();
                member.setPassword(passwordEncoder.encode(newPassword));
                memberEmailRepository.save(member);
                return true;
            }
        }
        return false;
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    private void sendEmail(String recipientEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ggim17861@gmail.com");
        message.setTo(recipientEmail);
        message.setSubject("Verification Code");
        message.setText("안녕하세요!\n\n" +
                "귀하의 인증 코드는 다음과 같습니다:\n" +
                "✅ " + verificationCode + "\n\n" +
                "이 코드를 사용하여 인증을 완료해 주세요.\n" +
                "감사합니다!");

        try {
            mailSender.send(message);
            System.out.println("✅ 이메일이 성공적으로 전송되었습니다: " + recipientEmail);
        } catch (MailSendException e) {
            System.err.println("❌ 이메일 전송 실패: " + e.getMessage());
        }
    }

    public boolean signupVerifyCode(String email, String verificationCode) {
        List<VerificationCode> codes = verificationCodeRepository.findByEmail(email);

        // 유효한 코드 찾기
        for (VerificationCode verificationCodeEntity : codes) {
            boolean isCodeValid = verificationCodeEntity.getCode().equals(verificationCode);
            boolean isCodeExpired = verificationCodeEntity.getSentAt().plusMinutes(5).isBefore(LocalDateTime.now());

            System.out.println("검증 코드: " + verificationCode);
            System.out.println("발송된 코드: " + verificationCodeEntity.getCode());
            System.out.println("만료 여부: " + isCodeExpired);

            if (isCodeValid) {
                if (!isCodeExpired) {
                    // 인증 성공 후 일정 시간 후에 삭제 처리
                    scheduleCodeDeletion(verificationCodeEntity);
                    return true;  // 인증 성공
                } else {
                    throw new IllegalArgumentException("인증 코드가 만료되었습니다.");
                }
            }
        }

        // 유효한 코드가 없는 경우
        throw new IllegalArgumentException("이 이메일로 전송된 인증 코드가 없습니다.");
    }

    private void scheduleCodeDeletion(VerificationCode verificationCodeEntity) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                verificationCodeRepository.delete(verificationCodeEntity);
            }
        }, 180000);
    }

}

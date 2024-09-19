package com.quantum.holdup.service;

import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

    @Autowired
    private MemberRepository memberRepository; // 사용자 정보를 데이터베이스에서 조회하는 레포지토리

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // 비밀번호 해싱을 위한 BCryptPasswordEncoder

    // 비밀번호 변경 로직을 처리하는 메서드
    public boolean resetPassword(String nickname, String email, String newPassword, EmailService emailService) {
        // 사용자 조회 (닉네임과 이메일로 사용자 찾기)
        Member member = memberRepository.findByNicknameAndEmail(nickname, email);
        if (member == null) {
            return false; // 사용자 존재하지 않음
        }

        // 비밀번호 해싱
        String hashedPassword = passwordEncoder.encode(newPassword);

        // 사용자 비밀번호 업데이트
        member.setPassword(hashedPassword);
        memberRepository.save(member);
        return true;
    }
}

package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.CreateMemberDTO;
import com.quantum.holdup.domain.dto.MemberDTO;
import com.quantum.holdup.domain.dto.SearchMemberEmailDTO;
import com.quantum.holdup.domain.dto.UpdateMemberDTO;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.Role;
import com.quantum.holdup.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repo;
    private final PasswordEncoder passwordEncoder;

    // 멤버 등록 메소드
    public CreateMemberDTO createMember(CreateMemberDTO memberInfo) {

        Member newMember = Member.builder()
                .email(memberInfo.getEmail())
                .password(passwordEncoder.encode(memberInfo.getPassword()))
                .nickname(memberInfo.getNickname())
                .phone(memberInfo.getPhone())
                .name(memberInfo.getName())
                .address(memberInfo.getAddress())
                .addressDetail(memberInfo.getAddressDetail())
                .birthday(memberInfo.getBirthday())
                .role(Role.USER)
                .build();

        repo.save(newMember);

        return new CreateMemberDTO(
                newMember.getEmail(),
                "", // 비밀번호는 클라이언트에 노출되지 않아야 하므로 빈 문자열로 설정
                newMember.getNickname(),
                newMember.getPhone(),
                newMember.getName(),
                newMember.getAddress(),
                newMember.getAddressDetail(),
                newMember.getBirthday()
        );
    }


    public String findEmailByNameAndPhone(SearchMemberEmailDTO searchMemberEmailDTO) {
        
        // 이름과 전화번호를 기준으로 사용자 조회
        Member member = repo.findByNameAndPhone(searchMemberEmailDTO.getName(),searchMemberEmailDTO.getPhone());

        // 사용자가 존재하면 이메일 반환, 없으면 "User not found" 반환
        if (member != null) {
            return member.getEmail();
        } else {
            return "User not found";
        }
    }

    public boolean isEmailAvailable(String email) {
        return !repo.existsByEmail(email);
    }

    public boolean isNicknameAvailable(String nickname) {
        return !repo.existsByNickname(nickname);
    }

    public boolean checkPassword(String email, String currentPassword) {
        // 이메일로 사용자 검색
        Member member = repo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. 이메일: " + email));

        // 비밀번호가 일치하는지 확인
        return passwordEncoder.matches(currentPassword, member.getPassword());
    }

    public void updateUserInfoByEmail(String email, UpdateMemberDTO memberDTO) {
        Member member = repo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. 이메일: " + email));

        // 현재 비밀번호 검증
        if (memberDTO.getCurrentPassword() != null && !passwordEncoder.matches(memberDTO.getCurrentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 빌더 패턴을 사용하여 업데이트할 필드들 설정
        Member updatedMember = member.toBuilder()
                .id(member.getId()) // 기존 ID 유지
                .nickname(memberDTO.getNickname() != null ? memberDTO.getNickname() : member.getNickname()) // 닉네임 업데이트
                .address(memberDTO.getAddress() != null ? memberDTO.getAddress() : member.getAddress()) // 주소 업데이트
                .addressDetail(memberDTO.getAddressDetail() != null ? memberDTO.getAddressDetail() : member.getAddressDetail()) // 주소 업데이트
                .password(memberDTO.getNewPassword() != null ? passwordEncoder.encode(memberDTO.getNewPassword()) : member.getPassword()) // 비밀번호 업데이트
                .entDate(member.getEntDate()) // 회원 가입일 유지
                .role(member.getRole()) // 역할 유지
                .credit(member.getCredit()) // 크레딧 유지
                .point(member.getPoint()) // 포인트 유지
                .isLeave(member.isLeave()) // 탈퇴 여부 유지
                .isBan(member.isBan()) // 정지 여부 유지
                .verificationCode(member.getVerificationCode()) // 인증 코드 유지
                .verificationCodeSentAt(member.getVerificationCodeSentAt()) // 인증 코드 발송 시간 유지
                .build();

        try {
            repo.save(updatedMember);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "데이터베이스 충돌이 발생했습니다. 입력 데이터를 확인하세요.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "회원 정보 저장 중 오류가 발생했습니다.", e);
        }
    }

    public void updateUser(String email, UpdateMemberDTO memberDTO) {
        updateUserInfoByEmail(email, memberDTO);
    }

    public boolean rechargeCredits(String email, int amount) {
        System.out.println("크레딧 충전 요청: 이메일 = " + email + ", 금액 = " + amount);

        // 금액 유효성 검사
        if (amount != 5000 && amount != 10000 && amount != 50000) {
            System.err.println("유효하지 않은 금액: " + amount);
            return false; // 유효하지 않은 금액
        }

        // 이메일로 회원 정보 조회
        Optional<Member> optionalMember = repo.findByEmail(email.trim()); // 공백 제거
        if (optionalMember.isEmpty()) {
            System.err.println("회원이 존재하지 않음: " + email); // 로그 출력
            return false; // 회원이 존재하지 않음
        }

        // 회원 정보 가져오기
        Member member = optionalMember.get();

        // 크레딧 충전
        int newCredit = member.getCredit() + amount; // 새로운 크레딧 계산
        System.out.println("현재 크레딧: " + member.getCredit()); // 현재 크레딧 출력
        System.out.println("추가할 금액: " + amount); // 추가할 금액 출력
        System.out.println("새로운 크레딧: " + newCredit); // 새로운 크레딧 로그 출력

        // 빌더 패턴을 사용하여 새로운 크레딧으로 업데이트
        member = member.toBuilder()
                .credit(newCredit) // 새로운 크레딧 설정
                .build();

        System.out.println("업데이트된 크레딧: " + member.getCredit()); // 크레딧 로그 출력

        // 변경 사항 저장
        try {
            repo.save(member); // 변경 사항 저장
            System.out.println("크레딧이 성공적으로 업데이트되었습니다."); // 성공 메시지
        } catch (Exception e) {
            System.err.println("크레딧 저장 중 오류 발생: " + e.getMessage());
            return false; // 저장 실패 시 false 반환
        }

        return true; // 성공적으로 충전됨
    }


}





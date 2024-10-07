package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.MemberDTO;
import com.quantum.holdup.domain.dto.UpdateMemberDTO;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 전체 멤버 조회 메소드
    public List<MemberDTO> findAllMember() {

        List<Member> memberEntityList = memberRepository.findAll(); // 레파지토리에서 모든 멤버를 엔티티로 받아옴

        List<MemberDTO> memberDTOList = memberEntityList.stream() // 엔티티리스트에 stream으로 모든 객체에 접근
                .map(memberEntity -> new MemberDTO( // 새로운 DTO를 만들고 안에 담을 내용 지정
                        memberEntity.getId(),
                        memberEntity.getEmail(),
                        memberEntity.getPassword(),
                        memberEntity.getNickname(),
                        memberEntity.getPhone(),
                        memberEntity.getName(),
                        memberEntity.getAddress(),
                        memberEntity.getAddressDetail(),
                        memberEntity.getBirthday(),
                        memberEntity.getCredit(),
                        memberEntity.getPoint(),
                        memberEntity.isLeave(),
                        memberEntity.isBan(),
                        memberEntity.getEntDate(), // 엔티티에서 가져온 값들을 DTO객체에 넣어줌
                        memberEntity.getRole()
                )).toList(); // 리스트로 변환

        return memberDTOList;
    }

    public void updateUser(String email, MemberDTO memberDTO) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. 이메일: " + email));


        // 빌더 패턴을 사용하여 업데이트할 필드들 설정
        Member updatedMember = member.toBuilder()
                .id(member.getId()) // 기존 ID 유지
                .nickname(memberDTO.getNickname() != null ? memberDTO.getNickname() : member.getNickname()) // 닉네임 업데이트
                .address(memberDTO.getAddress() != null ? memberDTO.getAddress() : member.getAddress()) // 주소 업데이트
                .addressDetail(memberDTO.getAddressDetail() != null ? memberDTO.getAddressDetail() : member.getAddressDetail()) // 주소 업데이트
                .entDate(member.getEntDate()) // 회원 가입일 유지
                .role(member.getRole() != null ? memberDTO.getRole() : member.getRole()) // 역할 유지
                .credit(member.getCredit()) // 크레딧 유지
                .point(member.getPoint()) // 포인트 유지
                .isLeave(member.isLeave()) // 탈퇴 여부 유지
                .isBan(member.isBan()) // 정지 여부 유지
                .verificationCode(member.getVerificationCode()) // 인증 코드 유지
                .verificationCodeSentAt(member.getVerificationCodeSentAt()) // 인증 코드 발송 시간 유지
                .build();

        try {
            memberRepository.save(updatedMember);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "데이터베이스 충돌이 발생했습니다. 입력 데이터를 확인하세요.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "회원 정보 저장 중 오류가 발생했습니다.", e);
        }
    }
}

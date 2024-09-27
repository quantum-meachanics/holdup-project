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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repo;
    private final PasswordEncoder passwordEncoder;

    // 전체 멤버 조회 메소드
    public List<MemberDTO> findAllMember() {

        List<Member> memberEntityList = repo.findAll(); // 레파지토리에서 모든 멤버를 엔티티로 받아옴

        List<MemberDTO> memberDTOList = memberEntityList.stream() // 엔티티리스트에 stream으로 모든 객체에 접근
                .map(memberEntity -> new MemberDTO( // 새로운 DTO를 만들고 안에 담을 내용 지정
                        memberEntity.getId(),
                        memberEntity.getEmail(),
                        memberEntity.getPassword(),
                        memberEntity.getNickname(),
                        memberEntity.getPhone(),
                        memberEntity.getName(),
                        memberEntity.getAddress(),
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

    // 멤버 등록 메소드
    public CreateMemberDTO createMember(CreateMemberDTO memberInfo) {

        Member newMember = Member.builder()
                .email(memberInfo.getEmail())
                .password(passwordEncoder.encode(memberInfo.getPassword()))
                .nickname(memberInfo.getNickname())
                .phone(memberInfo.getPhone())
                .name(memberInfo.getName())
                .address(memberInfo.getAddress())
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

    public void updateUserInfoByEmail(String email, UpdateMemberDTO memberDTO) {
        // 이메일로 사용자 찾기
        Member member = (Member) repo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. 이메일: " + email));

        // 닉네임 업데이트 (null이 아닐 경우)
        if (memberDTO.getNickname() != null) {
            member.setNickname(memberDTO.getNickname());
        }

        // 비밀번호 업데이트 (null이 아닐 경우)
        if (memberDTO.getPassword() != null) {
            member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        }

        // 변경 사항 저장
        repo.save(member);
    }
}





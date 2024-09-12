package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.CreateMemberDTO;
import com.quantum.holdup.domain.dto.LoginMemberDTO;
import com.quantum.holdup.domain.dto.MemberDTO;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.Role;
import com.quantum.holdup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private final MemberRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(memberInfo.getPassword());

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
                newMember.getPassword(),
                newMember.getNickname(),
                newMember.getPhone(),
                newMember.getName(),
                newMember.getAddress(),
                newMember.getBirthday()
                );
    }
}



package com.quantum.holdup.domain.dto;

import com.quantum.holdup.domain.entity.Role;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class MemberDTO {

    private long id; // 회원 아이디
    private String email; // 회원 이메일
    private String password; // 회원 비밀번호
    private String nickname; // 회원 닉네임
    private String phone; // 회원 휴대전화번호
    private String name; // 회원 본명
    private String address; // 회원 주소
    private String addressDetail;
    private LocalDate birthday; // 회원 생년월일
    private int credit; // 회원 보유 크레딧
    private int point; // 회원 보유 포인트
    private boolean isLeave; // 회원 탈퇴 여부
    private boolean isBan; // 회원 정지 여부
    private LocalDateTime entDate; // 회원가입일시
    private Role role; // 회원 등급

    @Builder
    public MemberDTO(long id, String email, String password, String nickname, String phone, String name, String address,String addressDetail , LocalDate birthday, int credit, int point, boolean isLeave, boolean isBan, LocalDateTime entDate, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.addressDetail = addressDetail;
        this.birthday = birthday;
        this.credit = credit;
        this.point = point;
        this.isLeave = isLeave;
        this.isBan = isBan;
        this.entDate = entDate;
        this.role = role;
    }
}

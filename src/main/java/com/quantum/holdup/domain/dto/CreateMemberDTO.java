package com.quantum.holdup.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class CreateMemberDTO {

    private String email; // 회원 이메일
    private String password; // 회원 비밀번호
    private String nickname; // 회원 닉네임
    private String phone; // 회원 휴대전화번호
    private String name; // 회원 본명
    private String address; // 회원 주소
    private LocalDate birthday; // 회원 생년월일
    @JsonIgnore // 이 필드는 JSON 변환 시 무시됩니다.
    private String addressDetail;
    @JsonIgnore // 이 필드는 JSON 변환 시 무시됩니다.
    private String confirmPassword;

    @Builder
    public CreateMemberDTO(String email, String password, String nickname, String phone, String name, String address, String addressDetail, String confirmPassword, LocalDate birthday) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.addressDetail = addressDetail;
        this.confirmPassword = confirmPassword;
        this.birthday = birthday;
    }
}
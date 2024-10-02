package com.quantum.holdup.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateMemberDTO {

    private String email; // 회원 이메일
    private String password; // 회원 비밀번호
    private String nickname; // 회원 닉네임
    private String phone; // 회원 휴대전화번호
    private String name; // 회원 본명
    private String address; // 회원 주소
    private String addressDetail;
    private LocalDate birthday; // 회원 생년월일

    @Builder
    public CreateMemberDTO(String email, String password, String nickname, String phone, String name, String address,String addressDetail, LocalDate birthday) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.addressDetail = addressDetail;
        this.birthday = birthday;
    }
}
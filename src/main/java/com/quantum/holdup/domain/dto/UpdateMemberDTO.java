package com.quantum.holdup.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateMemberDTO {

    private long id;
    private String email;
    private String password; // 회원 비밀번호
    private String nickname; // 회원 닉네임
    private String address; // 전체 주소 (address + addressDetail)
    private String addressDetail; // 상세 주소
    private String phone;
    private String newPassword; // 새 비밀번호
    private String currentPassword; // 현재 비밀번호

    @Builder
    public UpdateMemberDTO(long id ,String email, String password, String nickname, String address, String addressDetail, String phone, String newPassword, String currentPassword) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.address = address + " " + addressDetail; // 주소와 상세 주소 합치기
        this.addressDetail = addressDetail;
        this.phone = phone;
        this.newPassword = newPassword;
        this.currentPassword = currentPassword;
    }
}


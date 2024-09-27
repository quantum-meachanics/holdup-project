package com.quantum.holdup.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateMemberDTO {

//    private long id; // 회원 아이디
    private String email;
    private String password; // 회원 비밀번호
    private String nickname; // 회원 닉네임
    @Builder
//    public UpdateMemberDTO(long id, String email, String password, String nickname) {
    public UpdateMemberDTO(String email, String password, String nickname) {
//        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;

    }
}

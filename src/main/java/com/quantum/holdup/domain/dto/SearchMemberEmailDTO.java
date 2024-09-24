package com.quantum.holdup.domain.dto;


import lombok.*;

@NoArgsConstructor
@Getter
public class SearchMemberEmailDTO {

    private String name; // 회원 본명
    private String phone; // 회원 휴대전화번호

    @Builder
    public SearchMemberEmailDTO(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}

package com.quantum.holdup.domain.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class CreateInquiryDTO {

    private String title; // 문의글 제목
    private String content; // 문의글 본문
    private String nickname;

    @Builder
    public CreateInquiryDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

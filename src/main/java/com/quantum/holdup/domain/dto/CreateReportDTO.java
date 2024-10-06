package com.quantum.holdup.domain.dto;

import lombok.*;

@NoArgsConstructor
@Getter
public class CreateReportDTO {

    private String title; // 신고글 제목
    private String content; // 신고글 본문
    private String nickname;

    @Builder
    public CreateReportDTO(String title, String content, String nickname) {
        this.title = title;
        this.content = content;
        this.nickname = nickname;
    }
}

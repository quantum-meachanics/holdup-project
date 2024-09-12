package com.quantum.holdup.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateReportDTO {

    private String title; // 신고글 제목
    private String content; // 신고글 본문

    @Builder
    public UpdateReportDTO( String title, String content) {
        this.title = title;
        this.content = content;
    }
}

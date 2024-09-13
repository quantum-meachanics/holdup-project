package com.quantum.holdup.domain.dto;

import com.quantum.holdup.Page.PagingButtonInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReportDTO {

    private long id; // 신고글 아이디
    private String title; // 신고글 제목
    private String content; // 신고글 본문
    private String nickname;
    private PagingButtonInfo pagingInfo;

    @Builder
    public ReportDTO(long id, String title, String content, String nickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
    }
}

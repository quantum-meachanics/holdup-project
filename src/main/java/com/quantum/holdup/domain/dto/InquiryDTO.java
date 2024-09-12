package com.quantum.holdup.domain.dto;

import com.quantum.holdup.Page.PagingButtonInfo;
import com.quantum.holdup.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class InquiryDTO {

    private long id; // 문의글 아이디
    private String title; // 문의글 제목
    private String content; // 문의글 본문
    private String nickname;
    private PagingButtonInfo pagingInfo;

    @Builder
    public InquiryDTO(long id, String title, String content, String nickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
    }
}

package com.quantum.holdup.domain.dto;

import com.quantum.holdup.domain.entity.Reservation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class InquiryDetailDTO {

    private long id; // 리뷰 아이디
    private String title; // 리뷰 제목
    private String content; // 리뷰 본문
    private LocalDateTime createDate;
    private String nickname;
    private List<String> imageUrl;
    private List<Long> imageId;

    @Builder
    public InquiryDetailDTO(long id, String title, String content, LocalDateTime createDate, String nickname, List<String> imageUrl, List<Long> imageId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.imageId = imageId;
    }
}

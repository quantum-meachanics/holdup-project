package com.quantum.holdup.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateReviewDTO {

    private String title; // 리뷰 제목
    private String content; // 리뷰 본문
    private int rating; // 리뷰 별점
    private String nickname; // 리뷰 작성자 닉네임
    private long reservationId;

    @Builder
    public UpdateReviewDTO(String title, String content, int rating, long reservationId) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.reservationId = reservationId;
    }

}

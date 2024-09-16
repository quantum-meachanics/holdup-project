package com.quantum.holdup.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreateReviewDTO {

    private String title; // 리뷰 제목
    private String content; // 리뷰 본문
    private int rating; // 리뷰 별점
    private String nickname; // 리뷰 작성자 닉네임
    private long reservationId;

    public CreateReviewDTO(String title, String content, int rating) {
        this.title = title;
        this.content = content;
        this.rating = rating;
    }
}

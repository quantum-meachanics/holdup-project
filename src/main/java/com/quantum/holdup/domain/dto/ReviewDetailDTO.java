package com.quantum.holdup.domain.dto;

import com.quantum.holdup.domain.entity.Reservation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class ReviewDetailDTO {

    private long id; // 리뷰 아이디
    private String title; // 리뷰 제목
    private String content; // 리뷰 본문
    private int rating; // 리뷰 별점
    private LocalDateTime createDate;
    private Reservation reservation;
    private String nickname;
    private List<String> imageUrl;

    @Builder
    public ReviewDetailDTO(long id, String title, String content ,int rating, Reservation reservation, LocalDateTime createDate, String nickname, List<String> imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.reservation = reservation;
        this.createDate = createDate;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}

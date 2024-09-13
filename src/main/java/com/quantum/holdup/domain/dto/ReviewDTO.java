package com.quantum.holdup.domain.dto;

import com.quantum.holdup.Page.PagingButtonInfo;
import com.quantum.holdup.domain.entity.Reservation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewDTO {

    private long id; // 리뷰 아이디
    private String title; // 리뷰 제목
    private String content; // 리뷰 본문
    private int rating; // 리뷰 별점
    private Reservation reservation;
    private PagingButtonInfo pagingInfo;

    @Builder
    public ReviewDTO(long id, String title, String content ,int rating, Reservation reservation) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.reservation = reservation;
    }

}

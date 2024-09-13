package com.quantum.holdup.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 리뷰 아이디
    private String title; // 리뷰 제목
    private String content; // 리뷰 본문
    private boolean isHide; // 리뷰 숨기기 여부
    private int rating; // 리뷰 별점
    private LocalDateTime createDate; // 리뷰 작성일시

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member; // 리뷰글 작성자

    @ManyToOne
    @JoinColumn(name = "RESERVATION_ID")
    private Reservation reservation; // 예약내용

//    @OneToMany
//    @JoinColumn(name = "COMMENT_ID")
//    private List<Comment> comments; // 리뷰에 달린 댓글

    @PrePersist
    protected void onCreate() { // 생성일시를 자동으로 입력해주는 메소드
        this.createDate = LocalDateTime.now();
    }

    @Builder(toBuilder = true)
    public Review(long id, String title, String content, boolean isHide, int rating, Member member, Reservation reservation) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isHide = isHide;
        this.rating = rating;
        this.member = member;
        this.reservation = reservation;
    }
}

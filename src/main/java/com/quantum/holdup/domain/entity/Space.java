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
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 공간 아이디
    private String title; // 공간 제목
    private String address; // 공간 주소
    private String description; // 공간 설명
    private long width; // 공간 너비
    private long height; // 공간 높이
    private long depth; // 공간 깊이
    private int number; // 공간 갯수
    private int price; // 공간 가격
    private boolean isHide; // 공간 숨기기 여부
    private LocalDateTime createDate; // 공간 둥록일시

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member owner; // 공간 등록자

//    @OneToMany
//    @JoinColumn(name = "RESERVATION_ID")
//    private List<Reservation> reservations; // 공간에 생성된 예약

    @PrePersist
    protected void creatAt() { // 생성일시를 자동으로 입력해주는 메소드
        this.createDate = LocalDateTime.now();
    }

    @Builder(toBuilder = true)
    public Space(String title, String address, String description, long width, long height, long depth, int number, int price, Member owner) {
        this.title = title;
        this.address = address;
        this.description = description;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.number = number;
        this.price = price;
        this.owner = owner;

        this.isHide = false;
    }
}

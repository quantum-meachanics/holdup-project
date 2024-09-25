package com.quantum.holdup.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 예약 아이디
    private LocalDateTime startDate; // 이용 시작일시
    private LocalDateTime endDate; // 이용 종료일시
    private boolean isAccept; // 예약 접수 여부
    private boolean isEnd; // 예약 종료 여부
    private LocalDateTime createDate; // 예약 신청일시

    @ManyToOne
    @JoinColumn(name = "SPACE_ID")
    private Space space; // 예약 신청한 공간

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member client; // 예약 신청자

    @PrePersist
    protected void onCreate() { // 생성일시를 자동으로 입력해주는 메소드
        this.createDate = LocalDateTime.now();
    }

    @Builder(toBuilder = true)
    public Reservation(LocalDateTime startDate, LocalDateTime endDate, Space space, Member client) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.space = space;
        this.client = client;
        this.isAccept = false;
        this.isEnd = false;
    }

}

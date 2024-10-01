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

    @OneToOne(mappedBy = "reservation", // 예약 - 리뷰의 관계에서 예약이 주체를 가짐
            cascade = CascadeType.ALL, // 예약이 삭제되면 리뷰도 같이 삭제됨
            optional = true) // optional = 예약에 대한 리뷰가 존재하지 않아도 상관없음
    private Review review; // 예약에 작성된 리뷰

    @Builder(toBuilder = true)
    public Reservation(LocalDateTime startDate, LocalDateTime endDate, Space space, Member client) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.space = space;
        this.client = client;
        this.isAccept = false;
        this.isEnd = false;
    }

    // 생성일시를 자동으로 입력해주는 메소드
    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }
}

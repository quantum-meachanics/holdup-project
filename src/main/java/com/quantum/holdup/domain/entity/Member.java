package com.quantum.holdup.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 회원 아이디
    private String email; // 회원 이메일
    private String password; // 회원 비밀번호
    private String nickname; // 회원 닉네임
    private String phone; // 회원 휴대전화번호
    private String name; // 회원 본명
    private String address; // 회원 주소
    private LocalDate birthday; // 회원 생년월일
    private int credit; // 회원 보유 크레딧
    private int point; // 회원 보유 포인트
    private boolean isLeave; // 회원 탈퇴 여부
    private boolean isBan; // 회원 정지 여부
    private LocalDateTime entDate; // 회원가입일시

    @Enumerated(value = EnumType.STRING)
    private Role role; // 회원 등급

//    @OneToMany
//    @JoinColumn(name = "SPACE_ID")
//    private List<Space> spaces; // 회원이 등록한 공간

//    @OneToMany
//    @JoinColumn(name = "REVIEW_ID")
//    private List<Review> reviews; // 회원이 작성한 리뷰
//
//    @OneToMany
//    @JoinColumn(name = "INQUIRY_ID")
//    private List<Inquiry> inquiries; // 회원이 작성한 문의글
//
//    @OneToMany
//    @JoinColumn(name = "REPORT_ID")
//    private List<Report> reports; // 회원이 작성한 신고글
//
//    @OneToMany
//    @JoinColumn(name = "COMMENT_ID")
//    private List<Comment> comments; // 회원이 작성한 댓글
//
//    @OneToMany
//    @JoinColumn(name = "RESERVATION_ID")
//    private List<Reservation> reservations; // 회원이 신청한 예약

    @PrePersist
    protected void onCreate() { // 가입일시를 자동으로 입력해주는 메소드
        this.entDate = LocalDateTime.now();
    }

    @Builder(toBuilder = true)
    public Member(String email, String password, String nickname, String phone, String name, LocalDate birthday, Role role , String address) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.birthday = birthday;
        this.credit = 0;
        this.point = 0;
        this.isLeave = false;
        this.isBan = false;
        this.role = role;
    }

}

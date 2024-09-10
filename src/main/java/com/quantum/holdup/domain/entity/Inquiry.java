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
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 문의글 아이디
    private String title; // 문의글 제목
    private String content; // 문의글 본문
    private boolean isHide; // 문의글 숨기기 여부
    private LocalDateTime createDate; // 문의글 작성일시

    @PrePersist
    protected void onCreate() { // 작성일시를 자동으로 입력해주는 메소드
        this.createDate = LocalDateTime.now();
    }

    @Builder(toBuilder = true)
    public Inquiry(String title, String content) {
        this.title = title;
        this.content = content;
        this.isHide = false;
    }

}

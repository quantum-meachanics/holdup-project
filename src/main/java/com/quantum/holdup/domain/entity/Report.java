package com.quantum.holdup.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 신고글 아이디
    private String title; // 신고글 제목
    private String content; // 신고글 본문
    private boolean isHide; // 신고글 숨기기 여부
    private LocalDateTime createDate; // 신고글 작성일시

    @OneToMany
    @JoinColumn(name = "COMMENT_ID")
    private List<Comment> comments; // 신고글에 달린 댓글

    @PrePersist
    protected void onCreate() { // 생성일시를 자동으로 입력해주는 메소드
        this.createDate = LocalDateTime.now();
    }

    @Builder(toBuilder = true)
    public Report(String title, String content) {
        this.title = title;
        this.content = content;
        this.isHide = false;
    }

}

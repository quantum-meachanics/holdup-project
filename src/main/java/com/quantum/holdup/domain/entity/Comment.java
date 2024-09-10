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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 댓글아이디
    private String content; // 댓글내용
    private boolean isHide; // 댓글 숨기기 여부
    private LocalDateTime createDate; // 댓글 작성일시

    @PrePersist
    protected void onCreate() { // 작성일시를 자동으로 입력해주는 메소드
        this.createDate = LocalDateTime.now();
    }

    @Builder(toBuilder = true)
    public Comment(String content) {
        this.content = content;
        this.isHide = false;
    }

}

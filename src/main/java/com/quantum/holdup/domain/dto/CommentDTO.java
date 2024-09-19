package com.quantum.holdup.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.yaml.snakeyaml.comments.CommentType;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {

    private String content; // 댓글내용
    private LocalDateTime createDate; // 댓글 작성일시
    private String nickname; // 댓글 작성자

    private CommentType commentType;

    private long postId; // 게시글 아이디

    public enum CommentType {
        REVIEW,
        REPORT
    }


    @Builder
    public CommentDTO(String content) {
        this.content = content;

    }
}

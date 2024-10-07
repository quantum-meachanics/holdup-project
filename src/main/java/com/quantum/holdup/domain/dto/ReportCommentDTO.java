    package com.quantum.holdup.domain.dto;

    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.time.LocalDateTime;

    @NoArgsConstructor
    @Getter
    @Setter
    public class ReportCommentDTO {

        private String content; // 댓글내용
        private LocalDateTime createDate; // 댓글 작성일시
        private String nickname; // 댓글 작성자
        private long reportId; // 리뷰게시글 아이디



        @Builder
        public ReportCommentDTO(String content, String nickname, long reportId, LocalDateTime createDate) {
            this.content = content;
            this.nickname = nickname;
            this.reportId = reportId;
            this.createDate = createDate;

        }
    }

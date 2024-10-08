package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.ReportCommentDTO;
import com.quantum.holdup.domain.dto.ReviewCommentDTO;
import com.quantum.holdup.domain.entity.Comment;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.Report;
import com.quantum.holdup.domain.entity.Review;
import com.quantum.holdup.repository.CommentRepository;
import com.quantum.holdup.repository.MemberRepository;
import com.quantum.holdup.repository.ReportRepository;
import com.quantum.holdup.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepo;
    private final MemberRepository memberRepo;
    private final ReportRepository reportRepo;
    private final ReviewRepository reviewRepo;

    public ReportCommentDTO createReportComment(long id, ReportCommentDTO commentInfo) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Report report = reportRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with reportId " + id));

        Comment newComment = Comment.builder()
                .content(commentInfo.getContent())
                .member(member)
                .report(report)
                .createDate(commentInfo.getCreateDate())
                .build();

        commentRepo.save(newComment);

        return new ReportCommentDTO(newComment.getContent(),newComment.getMember().getNickname(),newComment.getReport().getId(),newComment.getCreateDate());
    }

    public ReviewCommentDTO createReviewComment(long id, ReviewCommentDTO commentInfo) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with reviewId " + id));

        Comment newComment = Comment.builder()
                .content(commentInfo.getContent())
                .member(member)
                .review(review)
                .createDate(commentInfo.getCreateDate())
                .build();

        commentRepo.save(newComment);

        return new ReviewCommentDTO(newComment.getContent(),newComment.getMember().getNickname(),newComment.getReview().getId(),newComment.getCreateDate());
    }


    public List<ReviewCommentDTO> findReviewComments(long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 가져온 이메일로 사용자 찾기
        Member member = memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 주어진 리뷰 ID에 해당하는 모든 댓글 조회
        List<Comment> comments = commentRepo.findByReviewId(id);

        // 댓글이 없을 경우 예외 처리
//        if (comments.isEmpty()) {
//            throw new NoSuchElementException("No comments found for review with id " + id);
//        }

        // 댓글 엔티티를 DTO로 변환하여 리스트로 반환
        return comments.stream()
                .map(comment -> ReviewCommentDTO.builder()
                        .content(comment.getContent())
                        .nickname(member.getNickname())
                        .createDate(comment.getCreateDate())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ReportCommentDTO> findReportComments(long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 가져온 이메일로 사용자 찾기
        Member member = memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 주어진 리뷰 ID에 해당하는 모든 댓글 조회
        List<Comment> comments = commentRepo.findByReportId(id);

        // 댓글이 없을 경우 예외 처리
        if (comments.isEmpty()) {
            throw new NoSuchElementException("No comments found for review with id " + id);
        }

        // 댓글 엔티티를 DTO로 변환하여 리스트로 반환
        return comments.stream()
                .map(comment -> ReportCommentDTO.builder()
                        .content(comment.getContent())
                        .nickname(member.getNickname())
                        .createDate(comment.getCreateDate())
                        .build())
                .collect(Collectors.toList());
    }
}

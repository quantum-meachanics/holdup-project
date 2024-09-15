package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.CommentDTO;
import com.quantum.holdup.domain.entity.Comment;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.Report;
import com.quantum.holdup.domain.entity.Review;
import com.quantum.holdup.repository.CommentRepository;
import com.quantum.holdup.repository.MemberRepository;
import com.quantum.holdup.repository.ReportRepository;
import com.quantum.holdup.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepo;
    private final MemberRepository memberRepo;
    private final ReportRepository reportRepo;
    private final ReviewRepository reviewRepo;

    public CommentDTO createReportComment(CommentDTO commentInfo) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = (Member) memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            Report report = reportRepo.findById(commentInfo.getPostId())
                    .orElseThrow(() -> new NoSuchElementException("Post not found with postId " + commentInfo.getPostId()));

        Comment newComment = Comment.builder()
                .content(commentInfo.getContent())
                .member(member)
                .report(report)
                .createDate(commentInfo.getCreateDate())
                .build();

        commentRepo.save(newComment);

        return new CommentDTO(newComment.getContent());
    }

    public CommentDTO createReviewComment(CommentDTO commentInfo) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = (Member) memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = reviewRepo.findById(commentInfo.getPostId())
                .orElseThrow(() -> new NoSuchElementException("Post not found with postId " + commentInfo.getPostId()));

        Comment newComment = Comment.builder()
                .content(commentInfo.getContent())
                .member(member)
                .review(review)
                .createDate(commentInfo.getCreateDate())
                .build();

        commentRepo.save(newComment);

        return new CommentDTO(newComment.getContent());
    }


}

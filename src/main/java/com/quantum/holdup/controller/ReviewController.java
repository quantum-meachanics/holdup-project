package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.*;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.CommentService;
import com.quantum.holdup.service.ReviewService;
import com.quantum.holdup.service.S3Service;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/holdup")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;
    private final CommentService commentService;
    private final S3Service s3Service;

    @GetMapping("/reviews")
    public ResponseEntity<ResponseMessage> findAllReview(@PageableDefault Pageable pageable){

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "리뷰 전체조회를 성공했습니다.",
                        service.findAllReview(pageable)
                ));
    }

    // 리뷰글 상세페이지
    @GetMapping("/reviews/{id}")
    public ResponseEntity<ResponseMessage> findReviewById(@PathVariable long id) {

        ReviewDTO reports = service.findReviewById(id);

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "아이디로 게시글 조회 성공"
                        , reports)
                );
    }

    // 리뷰글 추가
    @PostMapping(value = "/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReview( @RequestPart(value = "reviewInfo") CreateReviewDTO reviewInfo,
                                           @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        List<String> imageUrls = null;
        if (images!=null && !images.isEmpty()) {
            imageUrls = s3Service.uploadImage(images);
        }


        System.out.println("ReviewInfo ======================================> reviews Post 요청들어옴 : " + reviewInfo);
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "리뷰 등록에 성공하였습니다.",
                        service.createReview(reviewInfo, imageUrls)
                ));
    }

    // 리뷰글 수정
    @PutMapping("/reviews/{id}")
    public ResponseEntity<?> modifyReview(@PathVariable Long id, @RequestBody UpdateReviewDTO modifyInfo) {

        UpdateReviewDTO updatedPost = service.updateReview(id, modifyInfo);

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "게시글 수정 완료",
                        updatedPost
                ));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable long id) {

        Map<String, Object> responseMap = new HashMap<>();

        boolean isDeleted = service.deleteReview(id);

        String msg;

        if (isDeleted) {
            msg = "게시글 삭제에 성공하였습니다.";
        } else {
            msg = "게시글 삭제에 실패하였습니다.";
        }
        responseMap.put("result", msg);

        return ResponseEntity
                .ok()
                .body(new ResponseMessage(
                        "게시글 삭제 성공",
                        responseMap)
                );
    }

    // 댓글 추가
    @PostMapping("/reviews/{id}/comments")
    public ResponseEntity<?> createComment(@PathVariable long id ,@RequestBody CommentDTO commentInfo) {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "댓글 등록에 성공하였습니다.",
                        commentService.createReviewComment(id, commentInfo)
                ));
    }
}

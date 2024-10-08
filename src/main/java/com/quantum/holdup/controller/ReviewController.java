package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.*;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.CommentService;
import com.quantum.holdup.service.ReviewService;
import com.quantum.holdup.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    public ResponseEntity<ResponseMessage> findAllReview(@PageableDefault Pageable pageable) {

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "리뷰 전체조회를 성공했습니다.",
                        service.findAllReview(pageable)
                ));
    }

    // 리뷰글 상세페이지
    @GetMapping("/reviews/{id}")
    public ResponseEntity<ResponseMessage> detailReview(@PathVariable long id) {

        ReviewDetailDTO reviews = service.findReviewById(id);

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "아이디로 게시글 조회 성공"
                        , reviews)
                );
    }

    // 리뷰글 추가
    @PostMapping(value = "/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReview(@RequestPart(value = "reviewInfo") CreateReviewDTO reviewInfo,
                                          @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        List<String> imageUrls = s3Service.uploadImage(images);

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "리뷰 등록에 성공하였습니다.",
                        service.createReview(reviewInfo, imageUrls)
                ));
    }

    // 리뷰글 수정
    @PutMapping(value = "/reviews/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modifyReview(
            @PathVariable long id,
            @RequestPart(value = "modifyInfo", required = false) UpdateReviewDTO modifyInfo,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            @RequestPart(value = "deleteImage", required = false) List<Long> deleteImage
    ) {

        List<String> imageUrls = newImages != null ? s3Service.uploadImage(newImages) : new ArrayList<>();

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "게시글 수정 완료",
                        service.updateReview(id, modifyInfo, imageUrls, deleteImage)
                ));
    }


    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable long id) {
        System.out.println("======================== id" + id);
        Map<String, Object> responseMap = new HashMap<>();

        boolean isDeleted = service.deleteReview(id);

        String msg;

        if (isDeleted) {
            msg = "게시글 삭제에 성공하였습니다.";
            responseMap.put("result", msg);

            return ResponseEntity
                    .ok()
                    .body(new ResponseMessage(
                            "게시글 삭제 성공",
                            responseMap)
                    );
        } else {
            msg = "게시글 삭제에 실패하였습니다.";
            responseMap.put("result", msg);

            return ResponseEntity
                    .ok()
                    .body(new ResponseMessage(
                            "게시글 삭제 실패",
                            responseMap)
                    );
        }


    }

    // 댓글 추가
    @PostMapping("/reviews/{id}/comments")
    public ResponseEntity<?> createComment(@PathVariable long id, @RequestBody ReviewCommentDTO commentInfo) {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "댓글 등록에 성공하였습니다.",
                        commentService.createReviewComment(id, commentInfo)
                ));
    }

    // 댓글 조회
    @GetMapping("/reviews/{id}/comments")
    public ResponseEntity<ResponseMessage> findReviewComments(@PathVariable long id) {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "댓글 조회에 성공하였습니다.",
                        commentService.findReviewComments(id)
                ));
    }
}

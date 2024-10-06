package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.ReviewCommentDTO;
import com.quantum.holdup.domain.dto.CreateReportDTO;
import com.quantum.holdup.domain.dto.ReportDTO;
import com.quantum.holdup.domain.dto.UpdateReportDTO;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.CommentService;
import com.quantum.holdup.service.ReportService;
import com.quantum.holdup.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/holdup")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService service;
    private final CommentService commentService;
    private final S3Service s3Service;

    // 신고글 전체조회
    @GetMapping("/reports")
    public ResponseEntity<ResponseMessage> findAllReport(@PageableDefault Pageable pageable) {

//        Page<ReportDTO> reports;
//
//        if (nickname != null && !nickname.isEmpty() && "nickname".equals(searchType)) {
//            reports = service.searchByNickname(nickname, pageable);
//        } else {
//            reports = service.findAllReport(pageable);
//        }

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "신고 전체조회를 성공했습니다.",
                        service.findAllReport(pageable)
                ));
    }

    // 신고글 상세페이지
    @GetMapping("/reports/{id}")
    public ResponseEntity<ResponseMessage> findReportById(@PathVariable long id) {

        ReportDTO reports = service.findReportById(id);

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "아이디로 게시글 조회 성공"
                        , reports)
                );
    }

    // 신고글 추가
    @PostMapping(value = "/reports", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReport(@RequestPart(value = "reportInfo") CreateReportDTO reportInfo,
                                          @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        List<String> imageUrls = s3Service.uploadImage(images);

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "게시판 등록에 성공하였습니다.",
                        service.createReport(reportInfo, imageUrls)
                ));
    }



    // 신고글 수정
    @PutMapping("/reports/{id}")
    public ResponseEntity<?> modifyPost(@PathVariable long id, @RequestBody UpdateReportDTO modifyInfo) {

        UpdateReportDTO updatedPost = service.updateReport(id, modifyInfo);

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "게시글 수정 완료",
                        updatedPost)
                );
    }

    // 신고글 삭제
    @DeleteMapping("/reports/{id}")
    public ResponseEntity<?> deletePost(@PathVariable long id) {

        Map<String, Object> responseMap = new HashMap<>();

        boolean isDeleted = service.deleteReport(id);

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
    @PostMapping("/reports/{id}/comments")
    public ResponseEntity<?> createComment(@PathVariable long id ,@RequestBody ReviewCommentDTO commentInfo) {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "댓글 등록에 성공하였습니다.",
                        commentService.createReportComment(id,commentInfo)
                ));
    }
}

package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.*;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.InquiryService;
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
public class InquiryController {

    private final InquiryService service;
    private final S3Service s3Service;

    // 문의글 전체조회
    @GetMapping("/inquiries")
    public ResponseEntity<ResponseMessage> findAllInquiry(@PageableDefault Pageable pageable){

//        Page<InquiryDTO> inquiries;
//
//        if (nickname != null && !nickname.isEmpty() && "nickname".equals(searchType)) {
//            inquiries = service.searchByNickname(nickname, pageable);
//        } else {
//            inquiries = service.findAllInquiry(pageable);
//        }

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "문의 전체조회를 성공했습니다.",
                        service.findAllInquiry(pageable)
                ));
    }

    // 문의글 상세페이지
    @GetMapping("/inquiries/{id}")
    public ResponseEntity<ResponseMessage> detailInquiry(@PathVariable long id) {

        InquiryDetailDTO inquiries = service.findInquiryById(id);

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "아이디로 게시글 조회 성공"
                        , inquiries)
                );
    }

    // 문의글 추가
    @PostMapping(value = "/inquiries", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createInquiry(@RequestPart CreateInquiryDTO inquiryInfo,
                                           @RequestPart(value = "images", required = false)List<MultipartFile> images) {

        List<String> imageUrls = images != null ? s3Service.uploadImage(images) : new ArrayList<>();

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "문의 등록에 성공하였습니다.",
                        service.createInquiry(inquiryInfo, imageUrls)
                ));
    }

    // 문의글 수정
    @PutMapping(value = "/inquiries/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modifyPost(
            @PathVariable long id,
            @RequestPart(value = "modifyInfo", required = false) UpdateInquiryDTO modifyInfo,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            @RequestPart(value = "deleteImage", required = false) List<Long> deleteImage) {

        List<String> imageUrls = newImages != null ? s3Service.uploadImage(newImages) : new ArrayList<>();

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "게시글 수정 완료",
                        service.updateInquiry(id,modifyInfo,imageUrls,deleteImage))
                );
    }

    // 문의글 삭제
    @DeleteMapping("/inquiries/{id}")
    public ResponseEntity<?> deletePost(@PathVariable long id) {

        Map<String, Object> responseMap = new HashMap<>();

        boolean isDeleted = service.deleteInquiry(id);

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
}

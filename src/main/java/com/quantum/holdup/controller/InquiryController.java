package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.CreateInquiryDTO;
import com.quantum.holdup.domain.dto.InquiryDTO;
import com.quantum.holdup.domain.dto.UpdateInquiryDTO;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.InquiryService;
import com.quantum.holdup.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/inquires")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService service;

    // 문의글 전체조회
    @GetMapping("/inquiry")
    public ResponseEntity<ResponseMessage> findAllInquiry(@PageableDefault Pageable pageable,
                                                         @RequestParam(value = "nickname", required = false) String nickname,
                                                         @RequestParam(value = "searchType", required = false) String searchType){

        Page<InquiryDTO> inquiries;

        if (nickname != null && !nickname.isEmpty() && "nickname".equals(searchType)) {
            inquiries = service.searchByNickname(nickname, pageable);
        } else {
            inquiries = service.findAllInquiry(pageable);
        }

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "문의 전체조회를 성공했습니다.",
                        inquiries
                ));
    }

    // 문의글 추가
    @PostMapping("/inquiry")
    public ResponseEntity<?> createInquiry(@RequestBody CreateInquiryDTO inquiryInfo) {

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "게시판 등록에 성공하였습니다.",
                        service.createInquiry(inquiryInfo)
                ));
    }

    // 문의글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyPost(@PathVariable Long id, @RequestBody UpdateInquiryDTO modifyInfo) {

        UpdateInquiryDTO updatedPost = service.updateInquiry(id, modifyInfo);

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "게시글 수정 완료",
                        updatedPost)
                );
    }

    // 문의글 삭제
    @DeleteMapping("/{id}")
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

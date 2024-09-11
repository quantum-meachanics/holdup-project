package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.CreateReportDTO;
import com.quantum.holdup.domain.dto.ReportDTO;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService service;

    // 신고글 전체조회
    @GetMapping("/report")
    public ResponseEntity<ResponseMessage> findAllReport(@PageableDefault Pageable pageable, String memberId,
                                                         @RequestParam(value = "searchType", required = false) String searchType){

        Page<ReportDTO> reports;

        if (memberId != null && !memberId.isEmpty() && "memberId".equals(searchType)) {
            reports = service.searchByMemberId(memberId, pageable);
        } else {
            reports = service.findAllReport(pageable);
        }

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "신고 전체조회를 성공했습니다.",
                        reports
                ));
    }

    // 신고글 추가
    @PostMapping("/report")
    public ResponseEntity<?> createReport(@RequestBody CreateReportDTO reportInfo) {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "게시판 등록에 성공하였습니다.",
                        service.createReport(reportInfo)
                ));
    }

    // 신고글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> findReportById(@PathVariable long id) {

        ReportDTO reports = service.findReportById(id);

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "아이디로 게시글 조회 성공"
                        , reports)
                );
    }
}

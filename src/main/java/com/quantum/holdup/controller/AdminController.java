package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.MemberDTO;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.AdminMemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/holdup")
@RequiredArgsConstructor

public class AdminController {

    private final AdminMemberService service;

    @GetMapping("/admin/members")
    public ResponseEntity<ResponseMessage> findAllMembers() {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "전체 유저 조회를 성공했습니다.",
                        service.findAllMember()
                ));
    }

    @PutMapping("/admin/update/{email}")
    public ResponseEntity<ResponseMessage> updateMember(
            @PathVariable String email,
            @RequestBody MemberDTO memberDTO) {
        try {
            service.updateUser(email, memberDTO);
            return ResponseEntity.ok(new ResponseMessage("회원 정보가 수정되었습니다.", null));
        } catch (IllegalArgumentException e) {
            // 예외 처리: 잘못된 인자
            return ResponseEntity.badRequest().body(new ResponseMessage("회원 정보 수정 요청이 잘못되었습니다.", e.getMessage()));
        } catch (EntityNotFoundException e) {
            // 예외 처리: 회원을 찾을 수 없음
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("해당 회원을 찾을 수 없습니다.", e.getMessage()));
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("회원 정보 수정에 실패하였습니다.", e.getMessage()));
        }
    }


}

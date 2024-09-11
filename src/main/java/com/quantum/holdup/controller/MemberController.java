package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.CreateMemberDTO;
import com.quantum.holdup.domain.dto.LoginMemberDTO;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> findAllMembers() {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "전체 유저 조회를 성공했습니다.",
                        service.findAllMember()
                ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createMember(@RequestBody CreateMemberDTO memberInfo) {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "유저 등록에 성공하였습니다.",
                        service.createMember(memberInfo)
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody LoginMemberDTO login){
        return  ResponseEntity.ok()
                .body(new ResponseMessage(
                        "로그인에 성공하였습니다.",
                         service.login(login)
                ));
    }
}




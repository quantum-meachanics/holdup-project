package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.CreateMemberDTO;
import com.quantum.holdup.domain.dto.LoginMemberDTO;
import com.quantum.holdup.domain.dto.SearchMemberEmailDTO;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/holdup")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @GetMapping("/members")
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
    @CrossOrigin(origins = "http://localhost:3000")
    public void login(@RequestBody LoginMemberDTO loginInfo) {
    }


    @PostMapping("/find-email")
    public ResponseEntity<String> findEmail(@RequestBody SearchMemberEmailDTO searchMemberEmailDTO) {
        String email = service.findEmailByNameAndPhone(searchMemberEmailDTO);

        if (email != null) {
            return ResponseEntity.ok("유저 이메일을 성공적으로 찾았습니다: " + email);
        } else {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다."); // 404 Not Found
        }
    }


    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean available = service.isEmailAvailable(email); // 서비스 호출
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available); // 결과를 맵에 담아 반환
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam String nickname) {
        boolean available = service.isNicknameAvailable(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available); // true: 사용 가능, false: 사용 불가
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Member> getMemberInfo(@AuthenticationPrincipal Member member) {
        Member memberInfo = service.getMemberInfo(member.getId());
        return ResponseEntity.ok(memberInfo);
    }

}




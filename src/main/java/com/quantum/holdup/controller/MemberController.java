package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.*;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.CustomUserDetails;
import com.quantum.holdup.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PutMapping("/update")
    public ResponseEntity<?> updateUserInfo(@RequestBody UpdateMemberDTO memberDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자를 찾을 수 없습니다.");
            }

            String email = userDetails.getMember().getEmail(); // 현재 로그인한 사용자의 이메일 가져오기

            // 서비스 메서드를 호출하여 사용자 정보 업데이트
            service.updateUserInfoByEmail(email, memberDTO);
            return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 정보 수정 실패: " + e.getMessage());
        }
    }
}




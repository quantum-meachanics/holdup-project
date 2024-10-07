package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.*;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.CustomUserDetails;
import com.quantum.holdup.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/holdup")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

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

    @PutMapping("/update/{email}")
    public ResponseEntity<ResponseMessage> updateMember(
            @PathVariable String email,
            @RequestBody UpdateMemberDTO memberDTO) {
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

    @PostMapping("/check-password")
    public ResponseEntity<?> checkPassword(@RequestBody PasswordCheckRequestDTO request) {
        try {
            boolean isPasswordValid = service.checkPassword(request.getEmail(), request.getCurrentPassword());
            if (isPasswordValid) {
                return ResponseEntity.ok(new ResponseMessage("비밀번호가 확인되었습니다.", null));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("비밀번호가 일치하지 않습니다.", null));
            }
        } catch (Exception e) {
            // 예외 처리: 기타 예외
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("비밀번호 확인 중 오류가 발생했습니다.", e.getMessage()));
        }
    }

    @PutMapping("/{email}/recharge-credits")
    public ResponseEntity<String> rechargeCredits(@PathVariable String email, @RequestBody RechargeCreditsDTO rechargeCreditsDTO) {
        // DTO에서 금액을 가져오기
        String extractedEmail = rechargeCreditsDTO.getEmail(); // DTO에서 이메일 추출
        int amount = rechargeCreditsDTO.getAmount();

        // 이메일이 문자열 형태로 넘어오므로, 로그 출력
        System.out.println("크레딧 충전 요청: 이메일 = " + email + ", 금액 = " + amount + ", email" + extractedEmail);

        // 크레딧 충전 서비스 호출
        try {
            service.rechargeCredits(email, amount);
            return ResponseEntity.ok("크레딧이 성공적으로 충전되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("크레딧 충전 중 오류가 발생했습니다.");
        }
    }

}





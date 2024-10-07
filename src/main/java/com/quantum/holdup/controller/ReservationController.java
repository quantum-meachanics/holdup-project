package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.CreateReservationDTO;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/holdup")
@Slf4j
public class ReservationController {

    private final ReservationService service;

    // 예약 신청 메소드
    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@RequestBody CreateReservationDTO reservationInfo) {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "예약 신청 성공",
                        service.createReservation(reservationInfo)
                ));
    }

    // 내 예약 조회 메소드
    @GetMapping("/reservations")
    public ResponseEntity<ResponseMessage> getAllReservations(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "내 예약 조회를 성공했습니다.",
                        service.findMyReservations(pageable)
                ));
    }
}

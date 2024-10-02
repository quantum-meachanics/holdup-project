package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.CreateReservationDTO;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/holdup")
public class ReservationController {

    private final ReservationService service;

    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@RequestBody CreateReservationDTO reservationInfo) {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "예약 신청 성공",
                        service.createReservation(reservationInfo)
                ));
    }
}

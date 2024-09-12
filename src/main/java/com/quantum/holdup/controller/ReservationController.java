package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.CreateReservationDTO;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    @PostMapping("/{spaceId}")
    public ResponseEntity<?> createReservation(@PathVariable long spaceId, @RequestBody CreateReservationDTO reservationInfo) {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "예약 신청 성공",
                        service.createReservation(spaceId, reservationInfo)
                ));
    }
}

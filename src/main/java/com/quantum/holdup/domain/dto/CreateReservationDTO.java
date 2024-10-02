package com.quantum.holdup.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateReservationDTO {

    private long spaceId; // 예약할 공간 아이디
    private LocalDateTime startDateTime; // 이용 시작일시
    private LocalDateTime endDateTime; // 이용 종료일시

}

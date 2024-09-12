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

    private LocalDateTime startDate; // 이용 시작일시
    private LocalDateTime endDate; // 이용 종료일시

}

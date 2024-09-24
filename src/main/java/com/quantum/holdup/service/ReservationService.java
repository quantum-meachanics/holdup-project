package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.CreateReservationDTO;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.Reservation;
import com.quantum.holdup.domain.entity.Space;
import com.quantum.holdup.repository.MemberRepository;
import com.quantum.holdup.repository.ReservationRepository;
import com.quantum.holdup.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final SpaceRepository spaceRepo;
    private final MemberRepository memberRepo;

    // 예약 신청 메소드
    public CreateReservationDTO createReservation(long spaceId, CreateReservationDTO reservationInfo) {

        // 현재 로그인 되어있는 사용자의 이메일을 가져옴
        String clientEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 가져온 이메일로 신청자 찾기
        Member client = (Member) memberRepo.findByEmail("1")
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        // PathVariable 로 받아온 공간 아이디로 예약할 공간 찾기
        Space space = spaceRepo.findById(spaceId)
                .orElseThrow(() -> new NoSuchElementException("공간 정보를 찾을 수 없습니다."));

        // 새로운 예약 엔티티 생성
        Reservation newReservation = Reservation.builder()
                .startDate(reservationInfo.getStartDate())
                .endDate(reservationInfo.getEndDate())
                .space(space)
                .client(client)
                .build();

        // 새로운 예약 엔티티 저장
        reservationRepo.save(newReservation);

        // DTO 형식으로 반환
        return new CreateReservationDTO(
                newReservation.getStartDate(),
                newReservation.getEndDate()
        );
    }

}

package com.quantum.holdup.service;

import com.quantum.holdup.Page.Pagination;
import com.quantum.holdup.Page.PagingButtonInfo;
import com.quantum.holdup.domain.dto.CreateReservationDTO;
import com.quantum.holdup.domain.dto.ReservationListDTO;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.Reservation;
import com.quantum.holdup.domain.entity.Space;
import com.quantum.holdup.repository.MemberRepository;
import com.quantum.holdup.repository.ReservationRepository;
import com.quantum.holdup.repository.ReviewRepository;
import com.quantum.holdup.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final SpaceRepository spaceRepo;
    private final MemberRepository memberRepo;
    private final ReviewRepository reviewRepo;

    // 예약 신청 메소드
    public CreateReservationDTO createReservation(CreateReservationDTO reservationInfo) {

        // 현재 로그인 되어있는 사용자의 이메일을 가져옴
        String clientEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 가져온 이메일로 신청자 찾기
        Member client = memberRepo.findByEmail(clientEmail)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        // PathVariable 로 받아온 공간 아이디로 예약할 공간 찾기
        Space space = spaceRepo.findById(reservationInfo.getSpaceId())
                .orElseThrow(() -> new NoSuchElementException("공간 정보를 찾을 수 없습니다."));

        // 새로운 예약 엔티티 생성
        Reservation newReservation = Reservation.builder()
                .startDateTime(reservationInfo.getStartDateTime())
                .endDateTime(reservationInfo.getEndDateTime())
                .space(space)
                .client(client)
                .build();

        // 새로운 예약 엔티티 저장
        reservationRepo.save(newReservation);

        // DTO 형식으로 반환
        return new CreateReservationDTO(
                newReservation.getId(),
                newReservation.getStartDateTime(),
                newReservation.getEndDateTime()
        );
    }

    // 내가 신청한 예약 목록
    public Page<ReservationListDTO> findMyReservations(Pageable pageable) {

        // 로그인 되어있는 사용자의 이메일 가져옴
        String clientEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 가져온 이메일로 사용자 찾기
        Member client = memberRepo.findByEmail(clientEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("id").descending()
        );

        // 내가 신청한 예약들 가져오기
        Page<Reservation> myReservations = reservationRepo.findByClient(client, pageable);

        PagingButtonInfo paging = Pagination.getPagingButtonInfo(myReservations);

        return myReservations.map(myReservationEntity -> {

            // 예약한 공간 정보 가져오기
            Space space = spaceRepo.findById(myReservationEntity.getSpace().getId())
                    .orElseThrow(() -> new NoSuchElementException("공간 정보를 찾을 수 없습니다."));

            // 리뷰 작성 여부 확인
            boolean hasReview = reviewRepo.existsByReservationId(myReservationEntity.getId());

            ReservationListDTO reservationListDTO = new ReservationListDTO(
                    myReservationEntity.getId(),
                    myReservationEntity.getStartDateTime(),
                    myReservationEntity.getEndDateTime(),
                    myReservationEntity.isAccept(),
                    myReservationEntity.isEnd(),
                    myReservationEntity.getCreateDateTime(),
                    space.getId(),
                    space.getName(),
                    hasReview
            );

            // 페이징  정보 추가
            reservationListDTO.setPagingButtonInfo(paging);

            return reservationListDTO;
        });
    }
}
package com.quantum.holdup.service;

import com.quantum.holdup.Page.Pagination;
import com.quantum.holdup.Page.PagingButtonInfo;
import com.quantum.holdup.domain.dto.CreateReviewDTO;
import com.quantum.holdup.domain.dto.ReviewDTO;
import com.quantum.holdup.domain.dto.UpdateInquiryDTO;
import com.quantum.holdup.domain.dto.UpdateReviewDTO;
import com.quantum.holdup.domain.entity.Inquiry;
import com.quantum.holdup.domain.entity.Reservation;
import com.quantum.holdup.domain.entity.Review;
import com.quantum.holdup.repository.MemberRepository;
import com.quantum.holdup.repository.ReservationRepository;
import com.quantum.holdup.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository repo;
    private final ReservationRepository reservationRepo;

    public Page<ReviewDTO> findAllReview(Pageable pageable) {

        // 페이지 번호 조정 (0보다 크면 1을 빼고) 및 정렬 설정
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("id").descending()
        );

        // 레파지토리의 findAll 메소드를 사용하여 Review 엔티티의 페이지를 가져옴
        Page<Review> reviewEntityList = repo.findAll(pageable);

        // 가져온 페이지를 바탕으로 페이징 버튼 정보 생성
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(reviewEntityList);

        // Page<Review>를 Page<ReviewDTO>로 변환하고 페이징 정보 추가
        return reviewEntityList.map(reviewEntity -> {
            // 각 Review 엔티티에 대한 Reservation 조회
            Reservation reservation = reservationRepo.findById(reviewEntity.getReservation().getId())
                    .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다: " + reviewEntity.getReservation().getId()));

            // 각 Review 엔티티로부터 새로운 ReviewDTO 생성
            ReviewDTO reviewDTO = new ReviewDTO(
                    reviewEntity.getId(),
                    reviewEntity.getTitle(),
                    reviewEntity.getContent(),
                    reviewEntity.getRating(),
                    // 여기에 reservation 관련 필드 추가
                    reservation
            );

            // 각 ReviewDTO에 페이징 정보 설정
            reviewDTO.setPagingInfo(paging);
            return reviewDTO;
        });

    }

//    public Page<ReviewDTO> searchByNickname(String nickname, Pageable pageable) {
//
//        Page<Review> reviewEntityList = repo.findByMemberNickname(nickname, pageable);
//        PagingButtonInfo paging = Pagination.getPagingButtonInfo(reviewEntityList);
//
//        return reviewEntityList.map(reviewEntity -> {
//            // 각 Review 엔티티로부터 새로운 ReviewDTO 생성
//            ReviewDTO reviewDTO = new ReviewDTO(
//                    reviewEntity.getId(),
//                    reviewEntity.getTitle(),
//                    reviewEntity.getContent(),
//                    reviewEntity.getRating()
//            );
//
//            // 각 ReviewDTO에 페이징 정보 설정
//            reviewDTO.setPagingInfo(paging);
//            return reviewDTO;
//        });
//    }

    public Review createReview(CreateReviewDTO createReviewDTO) {

        log.info("🎃 review  생성 DTO : {}", createReviewDTO);

        Reservation reservation = reservationRepo.findById(createReviewDTO.getReservationId())
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다: " + createReviewDTO.getReservationId()));

        System.out.println(createReviewDTO);

        Review review = Review.builder()
                .reservation(reservation)
                .rating(createReviewDTO.getRating())
                .title(createReviewDTO.getTitle())
                .content(createReviewDTO.getContent())
                .build();

        return repo.save(review);
    }

    public UpdateReviewDTO updateReview(Long id, UpdateReviewDTO modifyInfo) {

        Review reviewEntity = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with postId " + id));

        // toBuilder()를 사용하여 기존 객체를 기반으로 새 객체 생성
        Review updatedReview = reviewEntity.toBuilder()
                .id(id)
                .title(modifyInfo.getTitle())
                .content(modifyInfo.getContent())
                .build();

        // 새로운 엔티티 저장
        repo.save(updatedReview);

        // ReviewDTO 생성 및 반환
        return new UpdateReviewDTO(updatedReview.getTitle(),updatedReview.getContent());
    }

    public boolean deleteReview(long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true; // 게시글 삭제 성공
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}

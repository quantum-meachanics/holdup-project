package com.quantum.holdup.service;

import com.quantum.holdup.Page.Pagination;
import com.quantum.holdup.Page.PagingButtonInfo;
import com.quantum.holdup.domain.dto.CreateReviewDTO;
import com.quantum.holdup.domain.dto.ReviewDTO;
import com.quantum.holdup.domain.dto.ReviewDetailDTO;
import com.quantum.holdup.domain.dto.UpdateReviewDTO;
import com.quantum.holdup.domain.entity.*;
import com.quantum.holdup.repository.MemberRepository;
import com.quantum.holdup.repository.ReservationRepository;
import com.quantum.holdup.repository.ReviewImageRepository;
import com.quantum.holdup.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository repo;
    private final ReservationRepository reservationRepo;
    private final MemberRepository memberRepo;
    private final ReviewImageRepository reviewImageRepo;
    private final S3Service s3Service;

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
                    reservation,
                    reviewEntity.getCreateDate(),
                    reviewEntity.getMember().getNickname()
            );

            // 각 ReviewDTO에 페이징 정보 설정
            reviewDTO.setPagingInfo(paging);
            return reviewDTO;
        });

    }

    public ReviewDetailDTO findReviewById(long id) {

        Review reviewEntity = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id " + id));

        List<String> imageUrls = reviewImageRepo.findByReviewId(id)
                .stream()
                .map(ReviewImage::getImageUrl)
                .toList();

        return ReviewDetailDTO.builder()
                .id(reviewEntity.getId())
                .title(reviewEntity.getTitle())
                .content(reviewEntity.getContent())
                .createDate(reviewEntity.getCreateDate())
                .rating(reviewEntity.getRating())
                .nickname(reviewEntity.getMember().getNickname())
                .reservation(reviewEntity.getReservation())
                .imageUrl(imageUrls)
                .build();
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
//                    reviewEntity.getName(),
//                    reviewEntity.getContent(),
//                    reviewEntity.getRating()
//            );
//
//            // 각 ReviewDTO에 페이징 정보 설정
//            reviewDTO.setPagingInfo(paging);
//            return reviewDTO;
//        });
//    }

    // 리뷰 게시글 추가
    public Object createReview(CreateReviewDTO reviewInfo, List<String> imageUrls) {

        // 로그인 되어있는 사용자의 이메일 가져옴
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 가져온 이메일로 사용자 CKWRL
        Member member = (Member ) memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reservation reservation = reservationRepo.findById(reviewInfo.getReservationId())
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다: " + reviewInfo.getReservationId()));


        Review review = Review.builder()
                .member(member)
                .reservation(reservation)
                .rating(reviewInfo.getRating())
                .title(reviewInfo.getTitle())
                .content(reviewInfo.getContent())
                .build();

        Review savedReview = repo.save(review);

        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<ReviewImage> images = imageUrls.stream().map(url -> ReviewImage.builder()
                    .imageUrl(url)
                    .imageName(extractFileNameFromUrl(url))
                    .review(savedReview)
                    .build()).toList();

            reviewImageRepo.saveAll(images);
        }


        // ReviewWithImageDTO 생성 및 반환
        return CreateReviewDTO.builder()
                .title(savedReview.getTitle())
                .content(savedReview.getContent())
                .rating(savedReview.getRating())
                .reservationId(reviewInfo.getReservationId())
                .build();

    }

    // url에서 이미지 name 추출
    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    // 리뷰 게시글 수정
    public UpdateReviewDTO updateReview(long id, UpdateReviewDTO modifyInfo,
                                        List<String> newImages,
                                        List<Long> deletedImageIds) {

        // 로그인 되어있는 사용자의 이메일 가져옴
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 가져온 이메일로 사용자 찾기
        Member member = (Member ) memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review reviewEntity = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with postId " + id));

        // 현재 사용자가 리뷰 작성자인지 확인합니다.
        // 작성자가 아니면 수정 권한이 없으므로 RuntimeException을 발생시킵니다.
        if (!reviewEntity.getMember().equals(member)) {
            throw new RuntimeException("You don't have permission to modify this review");
        }

        if (modifyInfo.getTitle() != null) {
            reviewEntity.setTitle(modifyInfo.getTitle());
        }
        if (modifyInfo.getContent() != null) {
            reviewEntity.setContent(modifyInfo.getContent());
        }
        if (modifyInfo.getRating() != 0) {
            reviewEntity.setRating(modifyInfo.getRating());
        }

        Review savedReview = repo.save(reviewEntity);

        // 기존 이미지를 삭제할 경우
        if (deletedImageIds != null && !deletedImageIds.isEmpty()) {
            for (Long imageId : deletedImageIds) {
                ReviewImage image = reviewImageRepo.findById(imageId)
                        .orElseThrow(() -> new NoSuchElementException("Image not found with id " + imageId));
                if (image.getReview().getId() == reviewEntity.getId()) {
                    String fileName = extractFileNameFromUrl(image.getImageUrl());
                    s3Service.deleteImage(fileName);
                    reviewImageRepo.delete(image);
                }
            }
        }

        // 새 이미지 추가
        if (newImages != null && !newImages.isEmpty()) {
            List<ReviewImage> images = newImages.stream().map(url -> ReviewImage.builder()
                    .imageUrl((url))
                    .imageName(extractFileNameFromUrl((url)))
                    .review(savedReview)
                    .build()).toList();

            reviewImageRepo.saveAll(images);
        }



        // ReviewDTO 생성 및 반환
        return new UpdateReviewDTO(savedReview.getTitle(), savedReview.getContent(),savedReview.getRating(),savedReview.getReservation().getId());
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

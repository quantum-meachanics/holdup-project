package com.quantum.holdup.service;

import com.quantum.holdup.Page.Pagination;
import com.quantum.holdup.Page.PagingButtonInfo;
import com.quantum.holdup.domain.dto.CreateReviewDTO;
import com.quantum.holdup.domain.dto.ReviewDTO;
import com.quantum.holdup.domain.dto.ReviewDetailDTO;
import com.quantum.holdup.domain.dto.UpdateReviewDTO;
import com.quantum.holdup.domain.entity.*;
import com.quantum.holdup.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CommentRepository commentRepo;
    private final ReservationRepository ReservationRepo;

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

        // 해당 리뷰의 이미지들 찾기
        List<ReviewImage> reviewImages = reviewImageRepo.findByReviewId(id);

        List<String> imageUrls = reviewImageRepo.findByReviewId(id)
                .stream()
                .map(ReviewImage::getImageUrl)
                .toList();

        List<Long> imageIds = reviewImages
                .stream()
                .map(ReviewImage::getId)
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
                .imageId(imageIds)
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

        // 가져온 이메일로 사용자 찾기
        Member member = memberRepo.findByEmail(email)
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
    public Object updateReview(long id, UpdateReviewDTO modifyInfo,
                               List<String> newImageUrls,
                               List<Long> deleteImageId) {

        // 로그인 되어있는 사용자의 이메일 가져옴
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 가져온 이메일로 사용자 찾기
        Member member = memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review reviewEntity = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with postId " + id));


        Review updatedReview = Review.builder()
                .id(reviewEntity.getId())
                .member(member)
                .reservation(reviewEntity.getReservation())
                .title(modifyInfo.getTitle())
                .content(modifyInfo.getContent())
                .rating(modifyInfo.getRating())
                .createDate(reviewEntity.getCreateDate())
                .build();

        Review savedReview = repo.save(updatedReview);

        // 기존 이미지를 삭제할 경우
        if (deleteImageId != null && !deleteImageId.isEmpty()) {

            List<ReviewImage> imagesToDelete = reviewImageRepo.findAllById(deleteImageId);

            for (ReviewImage image : imagesToDelete) {
                if (deleteImage(image.getId())) {
                    System.out.println("이미지 삭제 성공: " + image.getId());
                } else {
                    System.out.println("이미지 삭제 실패: " + image.getId());
                }
            }
        }

        // 새 이미지 추가
        if (newImageUrls != null && !newImageUrls.isEmpty()) {
            List<ReviewImage> images = newImageUrls.stream().map(url -> ReviewImage.builder()
                    .imageUrl((url))
                    .imageName(extractFileNameFromUrl((url)))
                    .review(savedReview)
                    .build()).toList();

            reviewImageRepo.saveAll(images);
        }


        // ReviewDTO 생성 및 반환
        return UpdateReviewDTO.builder()
                .title(savedReview.getTitle())
                .content(savedReview.getContent())
                .rating(savedReview.getRating())
                .reservationId(savedReview.getReservation().getId())
                .build();

    }

    // 이미지 삭제
    public boolean deleteImage(Long imageId) {
        try {
            // 1. DB에서 이미지 정보 조회
            ReviewImage image = reviewImageRepo.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));

            // 2. S3에서 이미지 파일 삭제
            String fileName = image.getImageName(); // S3에 저장된 파일 이름
            s3Service.deleteImage(fileName);

            // 3. DB에서 이미지 정보 삭제
            reviewImageRepo.delete(image);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 리뷰 삭제
    @Transactional
    public boolean deleteReview(long reviewId) {

        System.out.println("======================= 서비스 요청");

        try {
            Review review = repo.findById(reviewId)
                    .orElseThrow(() -> new NoSuchElementException("Review not found with id " + reviewId));

            System.out.println("=================== 아이디 찾기 id " + reviewId);

//            // 이미지 삭제 (이미지가 없어도 오류 발생하지 않음)
//            int deleteImage =  reviewImageRepo.deleteByReviewId(review.getId());
//            System.out.println("=========================== deleteImage" + review.getId() + deleteImage);
//
//            // 댓글 삭제 (댓글이 없어도 오류 발생하지 않음)
//            commentRepo.deleteByReviewId(review.getId());


            reviewImageRepo.deleteByReviewId(review.getId());
            System.out.println("=========================== 이미지 삭제 완료: reviewId = " + review.getId());

            // 댓글 삭제

            commentRepo.deleteByReviewId(review.getId());
            System.out.println("=========================== 댓글 삭제 완료: reviewId = " + review.getId());


            // 리뷰 삭제
            System.out.println("============ 리뷰삭제 전");

            repo.deleteReviewWithReservationJoin(reviewId);

//            repo.deleteById(reviewId);

            System.out.println("============ 리뷰삭제 reviewID" + reviewId);

            return true;  // 모든 작업이 성공적으로 완료되면 true 반환
        } catch (Exception e) {
            // 예외 발생 시 false 반환
            return false;
        }
    }
}

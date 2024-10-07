package com.quantum.holdup.repository;

import com.quantum.holdup.domain.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    List<ReviewImage> findByReviewId(long id);

//    Optional<ReviewImage> findByMemberId(Long id);

//    List<ReviewImage> findByMemberId(long id);
}

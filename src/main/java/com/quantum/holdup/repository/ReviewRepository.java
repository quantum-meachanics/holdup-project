package com.quantum.holdup.repository;

import com.quantum.holdup.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

//    Page<Review> findByMemberNickname(String nickname, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Review WHERE id = :reviewId AND reservation_id IN (SELECT id FROM Reservation)",nativeQuery = true)
    void deleteReviewWithReservationJoin(@Param("reviewId") Long reviewId);
}

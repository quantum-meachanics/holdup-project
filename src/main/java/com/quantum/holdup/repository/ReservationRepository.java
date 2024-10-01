package com.quantum.holdup.repository;

import com.quantum.holdup.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 리뷰의 총 갯수 조회
    @Query("SELECT COUNT(rv) " +
            "FROM Reservation r LEFT JOIN r.review rv " +
            "WHERE r.space.id = :spaceId")
    Integer countReviewsBySpaceId(@Param("spaceId") Long spaceId);

    // 별점 평균 조회
    @Query("SELECT AVG(rv.rating) " +
            "FROM Reservation r LEFT JOIN r.review rv " +
            "WHERE r.space.id = :spaceId")
    Long findAverageRatingBySpaceId(@Param("spaceId") Long spaceId);
}

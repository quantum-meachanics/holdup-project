package com.quantum.holdup.repository;

import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

//    Page<Report> findBynickname(String memberId, Pageable pageable);

    Page<Report> findByMember(Member member, Pageable pageable);
}

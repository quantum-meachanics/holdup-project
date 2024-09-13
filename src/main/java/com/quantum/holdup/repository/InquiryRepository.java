package com.quantum.holdup.repository;

import com.quantum.holdup.domain.entity.Inquiry;
import com.quantum.holdup.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    Page<Inquiry> findByMemberNickname(String nickname, Pageable pageable);
}

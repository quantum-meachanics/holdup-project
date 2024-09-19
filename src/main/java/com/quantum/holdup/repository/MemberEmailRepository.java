package com.quantum.holdup.repository;

import com.quantum.holdup.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberEmailRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);
}

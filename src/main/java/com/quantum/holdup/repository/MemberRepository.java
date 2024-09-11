package com.quantum.holdup.repository;

import com.quantum.holdup.domain.dto.LoginMemberDTO;
import com.quantum.holdup.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(LoginMemberDTO login);

    Optional<Object> findByEmail(String username);
}

package com.quantum.holdup.repository;

import com.quantum.holdup.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String username);
    Member findByNameAndPhone(String name, String phone);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}

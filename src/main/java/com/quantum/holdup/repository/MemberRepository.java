package com.quantum.holdup.repository;

import com.quantum.holdup.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Object> findByEmail(String username);
    Optional<Object> findByNickname(String nickname);

    Member findByNameAndPhone(String name, String phone);

    Member findByNicknameAndEmail(String nickname, String email);
}

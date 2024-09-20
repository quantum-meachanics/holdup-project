package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.CreateSpaceDTO;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.Space;
import com.quantum.holdup.repository.MemberRepository;
import com.quantum.holdup.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepo; // 공간 레파지토리
    private final MemberRepository memberRepo; // 멤버 레파지토리

    // 공간 등록 메소드
    public Object createSpace(CreateSpaceDTO spaceInfo) {

        // 로그인 되어있는 사용자의 이메일 가져옴
        String ownerEamil = SecurityContextHolder.getContext().getAuthentication().getName();

        // 가져온 이메일로 사용자 찾기
        Member owner = (Member) memberRepo.findByEmail(ownerEamil)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 새로운 공간 엔티티 생성
        Space newSpace = Space.builder()
                .name(spaceInfo.getName())
                .address(spaceInfo.getAddress())
                .detailAddress(spaceInfo.getDetailAddress())
                .gu(spaceInfo.getGu())
                .dong(spaceInfo.getDong())
                .description(spaceInfo.getDescription())
                .width(spaceInfo.getWidth())
                .height(spaceInfo.getHeight())
                .depth(spaceInfo.getDepth())
                .count(spaceInfo.getCount())
                .price(spaceInfo.getPrice())
                .owner(owner)
                .build();

        // 생성한 공간 엔티티 저장
        spaceRepo.save(newSpace);

        // DTO 형식으로 반환
        return new CreateSpaceDTO(
                newSpace.getName(),
                newSpace.getAddress(),
                newSpace.getDetailAddress(),
                newSpace.getGu(),
                newSpace.getDong(),
                newSpace.getDescription(),
                newSpace.getWidth(),
                newSpace.getHeight(),
                newSpace.getDepth(),
                newSpace.getCount(),
                newSpace.getPrice()
        );
    }
}

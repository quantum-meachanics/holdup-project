package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.CreateSpaceDTO;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.Space;
import com.quantum.holdup.domain.entity.SpaceImage;
import com.quantum.holdup.repository.MemberRepository;
import com.quantum.holdup.repository.SpaceImageRepository;
import com.quantum.holdup.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepo; // 공간 레파지토리
    private final MemberRepository memberRepo; // 멤버 레파지토리
    private final SpaceImageRepository imageRepo; // 공간 이미지 레파지토리

    // 공간 등록 메소드
    public Object createSpace(CreateSpaceDTO spaceInfo, List<String> imageUrls) {

        // 로그인 되어있는 사용자의 이메일 가져옴
        String ownerEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 가져온 이메일로 사용자 찾기
        Member owner = (Member) memberRepo.findByEmail(ownerEmail)
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

        // 이미지 담아줄 빈 리스트 생성
        List<SpaceImage> images = new ArrayList<>();

        if (imageUrls != null && !imageUrls.isEmpty()) {
            images = imageUrls.stream().map(url -> SpaceImage.builder()
                    .imageUrl(url)
                    .space(newSpace)
                    .build()).toList();

            imageRepo.saveAll(images);
        }

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
                newSpace.getPrice(),
                images
        );
    }
}

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

    private final SpaceRepository spaceRepo;
    private final MemberRepository memberRepo;

    public Object createSpace(CreateSpaceDTO spaceInfo) {

        String ownerEamil = SecurityContextHolder.getContext().getAuthentication().getName();

        Member owner = (Member) memberRepo.findByEmail(ownerEamil)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Space newSpace = Space.builder()
                .title(spaceInfo.getTitle())
                .address(spaceInfo.getAddress())
                .description(spaceInfo.getDescription())
                .width(spaceInfo.getWidth())
                .height(spaceInfo.getHeight())
                .depth(spaceInfo.getDepth())
                .number(spaceInfo.getNumber())
                .price(spaceInfo.getPrice())
                .owner(owner)
                .build();

        spaceRepo.save(newSpace);

        return new CreateSpaceDTO(
                newSpace.getTitle(),
                newSpace.getAddress(),
                newSpace.getDescription(),
                newSpace.getWidth(),
                newSpace.getHeight(),
                newSpace.getDepth(),
                newSpace.getNumber(),
                newSpace.getPrice(),
                newSpace.getOwner()
        );
    }
}

package com.quantum.holdup.service;

import com.quantum.holdup.domain.dto.CreateSpaceDTO;
import com.quantum.holdup.repository.MemberRepository;
import com.quantum.holdup.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepo;
    private final MemberRepository memberRepo;

    public Object createSpace(String ownerEmail, CreateSpaceDTO spaceInfo) {
        return null;
    }
}

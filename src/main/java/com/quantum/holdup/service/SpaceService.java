package com.quantum.holdup.service;

import com.quantum.holdup.Page.Pagination;
import com.quantum.holdup.Page.PagingButtonInfo;
import com.quantum.holdup.domain.dto.spaces.CreateSpaceDTO;
import com.quantum.holdup.domain.dto.spaces.SpaceDetailDTO;
import com.quantum.holdup.domain.dto.spaces.SpacePageDTO;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.Space;
import com.quantum.holdup.domain.entity.SpaceImage;
import com.quantum.holdup.repository.MemberRepository;
import com.quantum.holdup.repository.ReservationRepository;
import com.quantum.holdup.repository.SpaceImageRepository;
import com.quantum.holdup.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepo; // 공간 레파지토리
    private final MemberRepository memberRepo; // 멤버 레파지토리
    private final ReservationRepository reservationRepo; // 예약 레파지토리
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

        // 이미지 저장
        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<SpaceImage> images = imageUrls.stream().map(url -> SpaceImage.builder()
                    .imageUrl(url)
                    .imageName(extractFileNameFromUrl(url))
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
                newSpace.getPrice()
        );
    }

    // 이미지 name 추출하는 메소드
    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    // 공간 전체 페이징처리 하여 조회하는 메소드
    public Page<SpacePageDTO> findAllSpaces(Pageable pageable) {
        // 페이지의 번호 조정 및 정렬
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("id").descending()
        );

        // 전체 공간 엔티티 가져오기
        Page<Space> spaces = spaceRepo.findAll(pageable);

        // 가져온 엔티티 페이징 버튼 생성
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(spaces);

        return spaces.map(spaceEntity -> {
            Member member = memberRepo.findById(spaceEntity.getOwner().getId())
                    .orElseThrow(() -> new NoSuchElementException("공간 등록자 정보를 찾을 수 없습니다."));

            SpacePageDTO spacePageDTO = new SpacePageDTO(
                    spaceEntity.getId(),
                    member.getId(),
                    spaceEntity.getName(),
                    spaceEntity.getAddress(),
                    spaceEntity.getDetailAddress(),
                    spaceEntity.getGu(),
                    spaceEntity.getDong(),
                    spaceEntity.getDescription(),
                    spaceEntity.getWidth(),
                    spaceEntity.getHeight(),
                    spaceEntity.getDepth(),
                    spaceEntity.getCount(),
                    spaceEntity.getPrice(),
                    spaceEntity.isHide(),
                    spaceEntity.getCreateDate()
            );

            spacePageDTO.setPagingButtonInfo(paging);

            return spacePageDTO;
        });
    }

    // 공간 상세 조회 메소드
    public SpaceDetailDTO findSpaceById(long id) {

        // 아이디에 해당하는 공간 엔티티 찾아오기
        Space spaceEntity = spaceRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("아이디에 해당하는 공간을 찾을 수 없습니다."));

        //공간 등록자 엔티티 찾아오기
        Member ownerEntity = memberRepo.findById(spaceEntity.getOwner().getId())
                .orElseThrow(() -> new NoSuchElementException("공간 등록자 정보를 찾을 수 없습니다."));

        // 공간에 등록된 리뷰의 총 갯수 찾아오기
        int reviewCount = reservationRepo.countReviewsBySpaceId(id) != null ?
                reservationRepo.countReviewsBySpaceId(id) : 0;

        // 공간 리뷰의 별점 평균 찾아오기
        Long ratingAveraging = reservationRepo.findAverageRatingBySpaceId(id);
        long ratingAverage = (ratingAveraging != null) ? ratingAveraging : 0;

        // 공간에 등록된 이미지 찾아오기
        List<String> imageUrls = imageRepo.findBySpaceId(id)
                .stream().map(SpaceImage::getImageUrl).toList();

        // DTO에 담아서 반환
        return new SpaceDetailDTO(
                spaceEntity.getId(),
                spaceEntity.getCreateDate(),
                spaceEntity.getName(),
                spaceEntity.getAddress(),
                spaceEntity.getDetailAddress(),
                spaceEntity.getDescription(),
                spaceEntity.getWidth(),
                spaceEntity.getHeight(),
                spaceEntity.getDepth(),
                spaceEntity.getCount(),
                spaceEntity.getPrice(),
                ownerEntity.getNickname(),
                reviewCount,
                ratingAverage,
                imageUrls
        );
    }
}

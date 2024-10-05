package com.quantum.holdup.service;

import com.quantum.holdup.Page.Pagination;
import com.quantum.holdup.Page.PagingButtonInfo;
import com.quantum.holdup.domain.dto.*;
import com.quantum.holdup.domain.entity.*;
import com.quantum.holdup.repository.InquiryImageRepository;
import com.quantum.holdup.repository.InquiryRepository;
import com.quantum.holdup.repository.MemberRepository;
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
public class InquiryService {

    private final InquiryRepository repo;
    private final MemberRepository memberRepo;
    private final InquiryImageRepository inquiryImageRepo;
    private final S3Service s3Service;

    public Page<InquiryDTO> findAllInquiry(Pageable pageable) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 페이지 번호 조정 (0보다 크면 1을 빼고) 및 정렬 설정
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("id").descending()
        );

        // 레파지토리의 findAll 메소드를 사용하여 Inquiry 엔티티의 페이지를 가져옴
        Page<Inquiry> inquiriesEntityList = repo.findAll(pageable);

        // 가져온 페이지를 바탕으로 페이징 버튼 정보 생성
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(inquiriesEntityList);

        // Page<Inquiry>를 Page<InquiryDTO>로 변환하고 페이징 정보 추가
        return inquiriesEntityList.map(inquiryEntity -> {

            // 각 Inquiry 엔티티로부터 새로운 InquiryDTO 생성
            InquiryDTO inquiryDTO = InquiryDTO.builder()
                    .id(inquiryEntity.getId())
                    .title(inquiryEntity.getTitle())
                    .content(inquiryEntity.getContent())
                    .nickname(inquiryEntity.getMember().getNickname())
                    .createDate(inquiryEntity.getCreateDate())
                    .build();

            // 각 InquiryDTO에 페이징 정보 설정
            inquiryDTO.setPagingInfo(paging);
            return inquiryDTO;
        });

    }

    public InquiryDetailDTO findInquiryById(long id) {

        Inquiry inquiryEntity = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id " + id));

        // 해당 문의 이미지들 찾기
        List<InquiryImage> inquiryImages = inquiryImageRepo.findByInquiryId(id);

        List<String> imageUrls = inquiryImageRepo.findByInquiryId(id)
                .stream()
                .map(InquiryImage::getImageUrl)
                .toList();

        List<Long> imageIds = inquiryImages
                .stream()
                .map(InquiryImage::getId)
                .toList();

        return InquiryDetailDTO.builder()
                .id(inquiryEntity.getId())
                .title(inquiryEntity.getTitle())
                .content(inquiryEntity.getContent())
                .createDate(inquiryEntity.getCreateDate())
                .nickname(inquiryEntity.getMember().getNickname())
                .imageUrl(imageUrls)
                .imageId(imageIds)
                .build();
    }

//    public Page<InquiryDTO> searchByNickname(String nickname, Pageable pageable) {
//
//        Page<Inquiry> inquiriesEntityList = repo.findByMemberNickname(nickname, pageable);
//        PagingButtonInfo paging = Pagination.getPagingButtonInfo(inquiriesEntityList);
//
//        return inquiriesEntityList.map(inquiryEntity -> {
//            // 각 Inquiry 엔티티로부터 새로운 InquiryDTO 생성
//            InquiryDTO inquiryDTO = new InquiryDTO(
//                    inquiryEntity.getId(),
//                    inquiryEntity.getTitle(),
//                    inquiryEntity.getContent(),
//                    inquiryEntity.getMember().getNickname(),
//                    inquiryEntity.getCreateDate()
//            );
//
//            // 각 InquiryDTO에 페이징 정보 설정
//            inquiryDTO.setPagingInfo(paging);
//            return inquiryDTO;
//        });
//    }

    public Object createInquiry(CreateInquiryDTO reportInfo, List<String> imageUrls) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Inquiry newInquiry = Inquiry.builder()
                .title(reportInfo.getTitle())
                .content(reportInfo.getContent())
                .member(member)
                .build();

        Inquiry savedInquiry = repo.save(newInquiry);


        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<InquiryImage> images = imageUrls.stream().map(url -> InquiryImage.builder()
                    .imageUrl(url)
                    .imageName(extractFileNameFromUrl(url))
                    .inquiry(savedInquiry)
                    .build()).toList();

            inquiryImageRepo.saveAll(images);
        }

        return CreateInquiryDTO.builder()
                .title(savedInquiry.getTitle())
                .content(savedInquiry.getContent())
                .build();
    }

    // url에서 이미지 name 추출
    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    // 문의 수정
    public Object updateInquiry(long id, UpdateInquiryDTO modifyInfo,
                                List<String> newImageUrls,
                                List<Long> deleteImageId) {

        // 로그인 되어있는 사용자의 이메일 가져옴
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 가져온 이메일로 사용자 찾기
        Member member = memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Inquiry inquiryEntity = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with postId " + id));

        // toBuilder()를 사용하여 기존 객체를 기반으로 새 객체 생성
        Inquiry updatedInquiry = Inquiry.builder()
                .id(inquiryEntity.getId())
                .title(modifyInfo.getTitle())
                .member(member)
                .content(modifyInfo.getContent())
                .build();

        // 새로운 엔티티 저장
        Inquiry savedInquiry = repo.save(updatedInquiry);

        // 기존 이미지를 삭제할 경우
        if (deleteImageId != null && !deleteImageId.isEmpty()) {

            List<InquiryImage> imagesToDelete = inquiryImageRepo.findAllById(deleteImageId);

            for (InquiryImage image : imagesToDelete) {
                if (deleteImage(image.getId())) {
                    System.out.println("이미지 삭제 성공: " + image.getId());
                } else {
                    System.out.println("이미지 삭제 실패: " + image.getId());
                }
            }
        }

        // 새 이미지 추가
        if (newImageUrls != null && !newImageUrls.isEmpty()) {
            List<InquiryImage> images = newImageUrls.stream().map(url -> InquiryImage.builder()
                    .imageUrl((url))
                    .imageName(extractFileNameFromUrl((url)))
                    .inquiry(savedInquiry)
                    .build()).toList();

            inquiryImageRepo.saveAll(images);
        }

        // InquiryDTO 생성 및 반환
        return new UpdateInquiryDTO(updatedInquiry.getTitle(),updatedInquiry.getContent());
    }

    // 이미지 삭제 메서드
    public boolean deleteImage(Long imageId) {
        try {
            // 1. DB에서 이미지 정보 조회
            InquiryImage image = inquiryImageRepo.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));

            // 2. S3에서 이미지 파일 삭제
            String fileName = image.getImageName(); // S3에 저장된 파일 이름
            s3Service.deleteImage(fileName);

            // 3. DB에서 이미지 정보 삭제
            inquiryImageRepo.delete(image);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteInquiry(long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true; // 게시글 삭제 성공
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }



}

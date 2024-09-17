package com.quantum.holdup.service;

import com.quantum.holdup.Page.Pagination;
import com.quantum.holdup.Page.PagingButtonInfo;
import com.quantum.holdup.domain.dto.CreateInquiryDTO;
import com.quantum.holdup.domain.dto.InquiryDTO;
import com.quantum.holdup.domain.dto.UpdateInquiryDTO;
import com.quantum.holdup.domain.entity.Inquiry;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.repository.InquiryRepository;
import com.quantum.holdup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository repo;
    private final MemberRepository memberRepo;

    public Page<InquiryDTO> findAllInquiry(Pageable pageable) {

        // 페이지 번호 조정 (0보다 크면 1을 빼고) 및 정렬 설정
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("id").descending()
        );

        // 레파지토리의 findAll 메소드를 사용하여 Inquiry 엔티티의 페이지를 가져옴
        Page<Inquiry> inquiresEntityList = repo.findAll(pageable);

        // 가져온 페이지를 바탕으로 페이징 버튼 정보 생성
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(inquiresEntityList);

        // Page<Inquiry>를 Page<InquiryDTO>로 변환하고 페이징 정보 추가
        return inquiresEntityList.map(inquiryEntity -> {
            // 각 Inquiry 엔티티로부터 새로운 InquiryDTO 생성
            InquiryDTO inquiryDTO = new InquiryDTO(
                    inquiryEntity.getId(),
                    inquiryEntity.getTitle(),
                    inquiryEntity.getContent(),
                    inquiryEntity.getMember().getNickname()
            );

            // 각 InquiryDTO에 페이징 정보 설정
            inquiryDTO.setPagingInfo(paging);
            return inquiryDTO;
        });

    }

    public Page<InquiryDTO> searchByNickname(String nickname, Pageable pageable) {

        Page<Inquiry> inquiriesEntityList = repo.findByMemberNickname(nickname, pageable);
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(inquiriesEntityList);

        return inquiriesEntityList.map(inquiryEntity -> {
            // 각 Inquiry 엔티티로부터 새로운 InquiryDTO 생성
            InquiryDTO inquiryDTO = new InquiryDTO(
                    inquiryEntity.getId(),
                    inquiryEntity.getTitle(),
                    inquiryEntity.getContent(),
                    inquiryEntity.getMember().getNickname()
            );

            // 각 InquiryDTO에 페이징 정보 설정
            inquiryDTO.setPagingInfo(paging);
            return inquiryDTO;
        });
    }

    public CreateInquiryDTO createInquiry(CreateInquiryDTO reportInfo) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = (Member) memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Inquiry newInquiry = Inquiry.builder()
                .title(reportInfo.getTitle())
                .content(reportInfo.getContent())
                .member(member)
                .build();

        repo.save(newInquiry);

        return new CreateInquiryDTO(
                newInquiry.getTitle(),
                newInquiry.getContent()
        );
    }

    public UpdateInquiryDTO updateInquiry(long id, UpdateInquiryDTO modifyInfo) {

        Inquiry inquiryEntity = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with postId " + id));

        // toBuilder()를 사용하여 기존 객체를 기반으로 새 객체 생성
        Inquiry updatedInquiry = inquiryEntity.toBuilder()
                .id(id)
                .title(modifyInfo.getTitle())
                .content(modifyInfo.getContent())
                .build();

        // 새로운 엔티티 저장
        repo.save(updatedInquiry);

        // InquiryDTO 생성 및 반환
        return new UpdateInquiryDTO(updatedInquiry.getTitle(),updatedInquiry.getContent());
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

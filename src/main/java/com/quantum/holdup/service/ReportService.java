package com.quantum.holdup.service;

import com.quantum.holdup.Page.Pagination;
import com.quantum.holdup.Page.PagingButtonInfo;
import com.quantum.holdup.domain.dto.CreateReportDTO;
import com.quantum.holdup.domain.dto.ReportDTO;
import com.quantum.holdup.domain.dto.UpdateReportDTO;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.Report;
import com.quantum.holdup.repository.MemberRepository;
import com.quantum.holdup.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository repo;
    private final MemberRepository memberRepo;

    public Page<ReportDTO> findAllReport(Pageable pageable) {

        // 페이지 번호 조정 (0보다 크면 1을 빼고) 및 정렬 설정
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("id").descending()
        );

        // 레파지토리의 findAll 메소드를 사용하여 Report 엔티티의 페이지를 가져옴
        Page<Report> reportsEntityList = repo.findAll(pageable);

        // 가져온 페이지를 바탕으로 페이징 버튼 정보 생성
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(reportsEntityList);

        // Page<Report>를 Page<ReportDTO>로 변환하고 페이징 정보 추가
        return reportsEntityList.map(reportEntity -> {
            // 각 Report 엔티티로부터 새로운 ReportDTO 생성
            ReportDTO reportDTO = new ReportDTO(
                    reportEntity.getId(),
                    reportEntity.getTitle(),
                    reportEntity.getContent(),
                    reportEntity.getMember().getNickname()
            );

            // 각 ReportDTO에 페이징 정보 설정
            reportDTO.setPagingInfo(paging);
            return reportDTO;
        });

    }

    public ReportDTO findReportById(long id) {

        Report postEntity = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id " + id));

        return ReportDTO.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .build();
    }

    public Page<ReportDTO> searchByNickname(String nickname, Pageable pageable) {

        Page<Report> reportsEntityList = repo.findByMemberNickname(nickname, pageable);
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(reportsEntityList);

        return reportsEntityList.map(reportEntity -> {
            // 각 Report 엔티티로부터 새로운 ReportDTO 생성
            ReportDTO reportDTO = new ReportDTO(
                    reportEntity.getId(),
                    reportEntity.getTitle(),
                    reportEntity.getContent(),
                    reportEntity.getMember().getNickname()
            );

            // 각 ReportDTO에 페이징 정보 설정
            reportDTO.setPagingInfo(paging);
            return reportDTO;
        });

    }

    public CreateReportDTO createReport(CreateReportDTO reportInfo) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = (Member) memberRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Report newReport = Report.builder()
                .title(reportInfo.getTitle())
                .content(reportInfo.getContent())
                .member(member)
                .build();

        repo.save(newReport);

        return new CreateReportDTO(
                newReport.getTitle(),
                newReport.getContent()
        );
    }

    public UpdateReportDTO updateReport(Long id, UpdateReportDTO modifyInfo) {

        Report reportEntity = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with postId " + id));

        // toBuilder()를 사용하여 기존 객체를 기반으로 새 객체 생성
        Report updatedReport = reportEntity.toBuilder()
                .id(id)
                .title(modifyInfo.getTitle())
                .content(modifyInfo.getContent())
                .build();

        // 새로운 엔티티 저장
        repo.save(updatedReport);

        // ReportDTO 생성 및 반환
        return new UpdateReportDTO(updatedReport.getTitle(), updatedReport.getContent());
    }

    public boolean deleteReport(long id) {
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

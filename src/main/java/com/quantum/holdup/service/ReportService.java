package com.quantum.holdup.service;

import com.quantum.holdup.Page.Pagination;
import com.quantum.holdup.Page.PagingButtonInfo;
import com.quantum.holdup.domain.dto.CreateReportDTO;
import com.quantum.holdup.domain.dto.ReportDTO;
import com.quantum.holdup.domain.entity.Report;
import com.quantum.holdup.repository.MemberRepository;
import com.quantum.holdup.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository repo;
    private final MemberRepository mRepo;


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
                    reportEntity.getContent()
            );

            // 각 ReportDTO에 페이징 정보 설정
            reportDTO.setPagingInfo(paging);
            return reportDTO;
        });

    }

    public CreateReportDTO createReport(CreateReportDTO reportInfo) {

        Report newReport = Report.builder()
                .title(reportInfo.getTitle())
                .content(reportInfo.getContent())
                .build();

        repo.save(newReport);

        return new CreateReportDTO(
                newReport.getContent(),
                newReport.getContent()
        );
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

    public Page<ReportDTO> searchByMemberId(String memberId, Pageable pageable) {

        Page<Report> reportsEntityList = repo.findByMemberId(memberId, pageable);

        PagingButtonInfo paging = Pagination.getPagingButtonInfo(reportsEntityList);

        return reportsEntityList.map(reportEntity -> {
            ReportDTO reportDTO = new ReportDTO(
                    reportEntity.getId(),
                    reportEntity.getTitle(),
                    reportEntity.getContent()
            );
            reportDTO.setPagingInfo(paging);
            return reportDTO;
        });

    }
}

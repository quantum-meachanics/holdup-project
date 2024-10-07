package com.quantum.holdup.repository;

import com.quantum.holdup.domain.entity.ReportImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportImageRepository extends JpaRepository<ReportImage, Long> {
    List<ReportImage> findByReportId(long id);
}

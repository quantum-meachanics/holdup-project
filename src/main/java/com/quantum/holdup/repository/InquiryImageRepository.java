package com.quantum.holdup.repository;

import com.quantum.holdup.domain.entity.InquiryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryImageRepository extends JpaRepository<InquiryImage, Long> {
    List<InquiryImage> findByInquiryId(long id);

    void deleteByInquiryId(long id);
}

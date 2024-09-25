package com.quantum.holdup.domain.dto;

import com.quantum.holdup.domain.entity.ReviewImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter

public class ReviewWithImageDTO {

    private CreateReviewDTO reviewInfo;
    private List<String> imageUrls;

    @Builder
    public ReviewWithImageDTO(CreateReviewDTO reviewInfo, List<String> imageUrls) {
        this.reviewInfo = reviewInfo;
        this.imageUrls = imageUrls;
    }
}




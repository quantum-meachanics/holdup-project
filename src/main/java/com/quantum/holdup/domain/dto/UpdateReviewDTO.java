package com.quantum.holdup.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateReviewDTO {

    private String title;
    private String content;

    @Builder
    public UpdateReviewDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

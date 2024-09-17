package com.quantum.holdup.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateInquiryDTO {

    private String title; // 문의글 제목
    private String content; // 문의글 본문

}

package com.quantum.holdup.domain.dto;

import com.quantum.holdup.Page.PagingButtonInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateReportDTO {

    private String title; // 신고글 제목
    private String content; // 신고글 본문

}

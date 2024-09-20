package com.quantum.holdup.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateSpaceDTO {

    private String name; // 공간 이름
    private String address; // 공간 주소
    private String detailAddress; // 공간 상세 주소
    private String gu; // 공간 행정구
    private String dong; // 공간 행정동
    private String description; // 공간 설명
    private long width; // 공간 너비
    private long height; // 공간 높이
    private long depth; // 공간 깊이
    private int count; // 공간 갯수
    private int price; // 공간 가격

}

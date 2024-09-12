package com.quantum.holdup.domain.dto;

import com.quantum.holdup.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateSpaceDTO {

    private String title; // 공간 제목
    private String address; // 공간 주소
    private String description; // 공간 설명
    private long width; // 공간 너비
    private long depth; // 공간 깊이
    private long height; // 공간 높이
    private int number; // 공간 갯수
    private int price; // 공간 가격

    private Member owner; // 공간 등록자

}

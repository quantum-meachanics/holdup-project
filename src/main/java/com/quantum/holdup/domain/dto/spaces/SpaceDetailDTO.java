package com.quantum.holdup.domain.dto.spaces;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
// 기본생성자 필요없음
public class SpaceDetailDTO {

    private long id; // 공간 아이디
    private LocalDateTime createDate; // 공간 등록일시
    private String name; // 공간 이름
    private String address; // 공간 주소
    private String detailAddress; // 공간 상세 주소
    private String description; // 공간 설명
    private long width; // 공간 너비
    private long height; // 공간 높이
    private long depth; // 공간 깊이
    private int count; // 공간 갯수
    private int price; // 공간 가격

    private String ownerNickname; // 공간 등록자 닉네임
    private int reviewCount; // 공간에 등록된 리뷰수
    private long ratingAverage; // 공간 평균 별점
    private List<String> imageUrls; // 사진 URL들

}

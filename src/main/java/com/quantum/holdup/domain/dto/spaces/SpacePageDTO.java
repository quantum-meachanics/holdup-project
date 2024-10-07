package com.quantum.holdup.domain.dto.spaces;

import com.quantum.holdup.Page.PagingButtonInfo;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public class SpacePageDTO {

    private long id; // 공간 아이디
    private String name; // 공간 이름
    private String gu; // 공간 행정구
    private String dong; // 공간 행정동
    private long width; // 공간 너비
    private long height; // 공간 높이
    private long depth; // 공간 깊이
    private int count; // 공간 갯수
    private int price; // 공간 가격
    private boolean isHide; // 공간 숨기기 여부
    private LocalDateTime createDate; // 공간 둥록일시

    private String imageUrl; // 대표이미지 URL

    private long ownerId; // 공간 등록자 아이디
    private String ownerNickname; // 공간 등록자 닉네임

    private long ratingAverage; // 별점 평균
    private int reviewCount; // 리뷰 갯수

    private PagingButtonInfo pagingButtonInfo;

    @Builder(toBuilder = true)
    public SpacePageDTO(long id, String name, String gu, String dong, long width, long height, long depth, int count, int price, boolean isHide, LocalDateTime createDate, String imageUrl, long ownerId, String ownerNickname, long ratingAverage, int reviewCount) {
        this.id = id;
        this.name = name;
        this.gu = gu;
        this.dong = dong;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.count = count;
        this.price = price;
        this.isHide = isHide;
        this.createDate = createDate;
        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
        this.ownerNickname = ownerNickname;
        this.ratingAverage = ratingAverage;
        this.reviewCount = reviewCount;
    }
}

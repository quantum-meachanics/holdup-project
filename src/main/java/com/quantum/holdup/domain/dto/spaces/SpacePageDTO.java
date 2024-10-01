package com.quantum.holdup.domain.dto.spaces;

import com.quantum.holdup.Page.PagingButtonInfo;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class SpacePageDTO {

    private long id; // 공간 아이디
    private long ownerId; // 공간 등록자 아이디
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
    private boolean isHide; // 공간 숨기기 여부
    private LocalDateTime createDate; // 공간 둥록일시
    private PagingButtonInfo pagingButtonInfo;

    @Builder(toBuilder = true)
    public SpacePageDTO(long id, long ownerId, String name, String address, String detailAddress, String gu, String dong, String description, long width, long height, long depth, int count, int price, boolean isHide, LocalDateTime createDate) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.detailAddress = detailAddress;
        this.gu = gu;
        this.dong = dong;
        this.description = description;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.count = count;
        this.price = price;
        this.isHide = isHide;
        this.createDate = createDate;
    }
}

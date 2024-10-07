package com.quantum.holdup.domain.dto;

import com.quantum.holdup.Page.PagingButtonInfo;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class ReservationListDTO {

    private long id; // 예약 아이디
    private LocalDateTime startDateTime; // 이용 시작일시
    private LocalDateTime endDateTime; // 이용 종료일시
    private boolean isAccept; // 예약 접수 여부
    private boolean isEnd; // 예약 종료 여부
    private LocalDateTime createDateTime; // 예약 신청일시

    private long spaceId; // 예약한 공간 아이디
    private String spaceName; // 예약한 공간 이름

    private long ownerId; // 공간 주인 아이디
    private String ownerNickname; // 공간 주인 닉네임

    private PagingButtonInfo pagingButtonInfo; // 페이징 처리

    @Builder(toBuilder = true)
    public ReservationListDTO(long id, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isAccept, boolean isEnd, LocalDateTime createDateTime, long spaceId, String spaceName, long ownerId, String ownerNickname) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.isAccept = isAccept;
        this.isEnd = isEnd;
        this.createDateTime = createDateTime;
        this.spaceId = spaceId;
        this.spaceName = spaceName;
        this.ownerId = ownerId;
        this.ownerNickname = ownerNickname;
    }
}

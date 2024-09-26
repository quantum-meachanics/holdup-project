package com.quantum.holdup.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SpaceImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 이미지 아이디

    @Column(nullable = false)
    private String imageUrl; // 이미지 URL

    @ManyToOne
    @JoinColumn(name = "SPACE_ID")
    private Space space; // 첨부된 공간

    @Builder
    public SpaceImage(String imageUrl, Space space) {
        this.imageUrl = imageUrl;
        this.space = space;
    }
}

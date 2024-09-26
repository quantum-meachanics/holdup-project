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
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "REVIEW_ID")
    private Review review;

    @Builder
    public ReviewImage(String fileName, String imageUrl, Review review) {
        this.fileName = fileName;
        this.imageUrl = imageUrl;
        this.review = review;
    }
}

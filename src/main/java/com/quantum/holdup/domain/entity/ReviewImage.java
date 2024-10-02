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
    private String imageName;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "REVIEW_ID")
    private Review review;

    @Builder
    public ReviewImage(long id ,String imageUrl, Review review, String imageName) {
        this.id = id;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.review = review;
    }
}

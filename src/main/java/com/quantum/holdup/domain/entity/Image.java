package com.quantum.holdup.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "REVIEW_ID")
    private Review review;

    @Builder
    public Image(Long id, String imageUrl, Review review) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.review = review;
    }
}

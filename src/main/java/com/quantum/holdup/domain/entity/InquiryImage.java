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
public class InquiryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "INQUIRY_ID")
    private Inquiry inquiry;

    @Builder
    public InquiryImage(long id , String imageUrl, Inquiry inquiry, String imageName) {
        this.id = id;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.inquiry = inquiry;
    }
}

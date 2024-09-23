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

    private String fileName;
    private String filePath;
    private String fileType;
    private long size;

    @ManyToOne
    @JoinColumn(name = "REVIEW_ID")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "SPACE_ID")
    private Space space;

    @Builder
    public Image(Long id, String fileName, String filePath, String fileType, long size, Review review) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.size = size;
        this.review = review;
    }
}

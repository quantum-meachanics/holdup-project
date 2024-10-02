package com.quantum.holdup.repository;

import com.quantum.holdup.domain.entity.SpaceImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceImageRepository extends JpaRepository<SpaceImage, Long> {

    List<SpaceImage> findBySpaceId(long spaceId);
}

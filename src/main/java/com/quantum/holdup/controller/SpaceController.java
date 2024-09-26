package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.CreateSpaceDTO;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.S3Service;
import com.quantum.holdup.service.SpaceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/holdup")
public class SpaceController {

    private final SpaceService spaceService;
    private final S3Service s3Service;

    @Operation(summary = "공간 등록")
    @PostMapping(value = "/spaces", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createSpace(
            @RequestPart(value = "spaceInfo") CreateSpaceDTO spaceInfo,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        List<String> imageUrls = s3Service.uploadImage(images);

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "공간 등록 성공",
                        spaceService.createSpace(spaceInfo, imageUrls)
                ));
    }
}

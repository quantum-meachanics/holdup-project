package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.spaces.CreateSpaceDTO;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.S3Service;
import com.quantum.holdup.service.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/holdup")
public class SpaceController {

    private final SpaceService spaceService;
    private final S3Service s3Service;

    // 공간 등록
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

    // 공간 전체 조회 페이징
    @GetMapping("/spaces")
    public ResponseEntity<ResponseMessage> findAllSpaces(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "공간 전체 조회를 성공했습니다.",
                        spaceService.findAllSpaces(pageable)
                ));
    }

    // 아이디로 공간 상세 조회
    @GetMapping("/spaces/{id}")
    public ResponseEntity<ResponseMessage> findSpaceById(@PathVariable long id) {
        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "공간 상세 조회를 성공했습니다.",
                        spaceService.findSpaceById(id)
                ));
    }
}

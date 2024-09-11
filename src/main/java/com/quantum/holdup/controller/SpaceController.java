package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.CreateSpaceDTO;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.SpaceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spaces")
public class SpaceController {

    private final SpaceService service;

    @Operation(summary = "공간 등록")
    @PostMapping("/createSpace")
    public ResponseEntity<?> createSpace(Authentication auth, @RequestBody CreateSpaceDTO spaceInfo) {

        String ownerEmail = auth.getName();

        return ResponseEntity.ok()
                .body(new ResponseMessage(
                        "공간 등록 성공",
                        service.createSpace(ownerEmail, spaceInfo)
                ));
    }

}

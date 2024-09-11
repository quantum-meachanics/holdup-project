package com.quantum.holdup.controller;

import com.quantum.holdup.domain.dto.CreateSpaceDTO;
import com.quantum.holdup.message.ResponseMessage;
import com.quantum.holdup.service.SpaceService;
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

    @PostMapping
    public ResponseEntity<?> createSpace(Authentication auth, @RequestBody CreateSpaceDTO spaceInfo) {

        System.out.println(auth.getName());

        return null;
    }

}

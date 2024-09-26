package com.quantum.holdup.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 이미지 업로드 메소드
    public List<String> uploadImage(List<MultipartFile> multipartFile) {

        List<String> fileUrlList = new ArrayList<>(); // 파일 이름 저장할 빈 리스트 생성

        multipartFile.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename()); // 파일 이름 생성
            ObjectMetadata objectMetadata = new ObjectMetadata(); // objectMeatdata를 통해,
            objectMetadata.setContentLength(file.getSize()); // 파일 크기 설정,
            objectMetadata.setContentType(file.getContentType()); // 파일 타입 설정

            try (InputStream inputStream = file.getInputStream()) { // InputStream으로 파일 읽음
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata) // 위에서 설정한 값들 전달
                        .withCannedAcl(CannedAccessControlList.PublicRead)); // 파일 접근권한 공개로 설정

                String fileUrl = amazonS3.getUrl(bucket, fileName).toString();
                fileUrlList.add(fileUrl);

            } catch (IOException e) { // Exception 처리
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
            }

        });

        return fileUrlList;
    }

    // 파일 삭제 메소드
    public void deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    // 파일 이름 생성 메소드
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString() // UUID로 고유식별자 생성
                .concat(getFileExtension(fileName)); // 파일확장자 붙여줌
    }

    // 파일 확장자 추출 메소드
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) { // 잘못된 확장자 처리
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
}


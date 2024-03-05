package com.ZeroWaveV2.FireAlertV2.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        // 파일 저장 로직 (생략)
        String fileName = file.getOriginalFilename();
        // 파일을 서버의 지정된 위치에 저장하는 로직을 구현해야 합니다.
        // 저장 후 파일에 접근 가능한 URL을 생성합니다.
        String imgurl = "http://localhost:8081/uploaded/" + fileName;
        return ResponseEntity.ok(Map.of("imgurl", imgurl));
    }
}
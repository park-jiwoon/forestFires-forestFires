package com.ZeroWaveV2.FireAlertV2.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ZeroWaveV2.FireAlertV2.dto.NoticeDto;
import com.ZeroWaveV2.FireAlertV2.model.Notice;
import com.ZeroWaveV2.FireAlertV2.service.NoticeService;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;
    
    // 공지사항 작성
    @PostMapping
    public ResponseEntity<Notice> createNotice(@RequestParam("title") String title,
                                               @RequestParam("post") String post,
                                               @RequestParam("image") MultipartFile image) {
        try {
            NoticeDto noticeDto = new NoticeDto();
            noticeDto.setTitle(title);
            noticeDto.setPost(post);
            Notice notice = noticeService.createNotice(noticeDto, image);
            return ResponseEntity.ok(notice);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 공지사항 번호, 제목, 생성일자 가져오기
    @GetMapping
    public ResponseEntity<List<NoticeDto>> getAllNotices() {
        List<NoticeDto> notices = noticeService.findNoticeSummary();
        return ResponseEntity.ok(notices);
    }
    
    // 번호로 공지사항 읽기
    @GetMapping("/{num}")
    public ResponseEntity<NoticeDto> getNoticeByNum(@PathVariable int num) {
        NoticeDto noticeDto = noticeService.getNoticeByNum(num);
        if (noticeDto != null) {
            return ResponseEntity.ok(noticeDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 공지사항 수정
    @PutMapping("/{num}")
    public ResponseEntity<NoticeDto> updateNotice(
            @PathVariable(value = "num") int num,
            @RequestParam("title") String title,
            @RequestParam("post") String post,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            NoticeDto noticeDto = new NoticeDto();
            noticeDto.setTitle(title);
            noticeDto.setPost(post);
            // Assuming NoticeDto has a constructor without image or setters are used here

            NoticeDto updatedNoticeDto = noticeService.updateNotice(num, noticeDto, image);
            return ResponseEntity.ok(updatedNoticeDto);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 공지사항 삭제
    @DeleteMapping("/{num}")
    public ResponseEntity<Void> deleteNotice(@PathVariable int num) {
        noticeService.deleteNotice(num);
        return ResponseEntity.ok().build(); // 성공적으로 삭제되었음을 나타냅니다.
    }
    
    // 조회 수 증가
    @GetMapping("/increaseHit/{num}")
    public ResponseEntity<Void> increaseHit(@PathVariable int num) {
        noticeService.increaseNoticeHit(num);
        return ResponseEntity.ok().build();
    }    
    
    @GetMapping("/previous/{num}")
    public ResponseEntity<?> findPreviousNotice(@PathVariable int num) {
        return noticeService.findPreviousNotice(num)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/next/{num}")
    public ResponseEntity<?> findNextNotice(@PathVariable int num) {
        return noticeService.findNextNotice(num)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
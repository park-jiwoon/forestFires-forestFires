package com.ZeroWaveV2.FireAlertV2.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ZeroWaveV2.FireAlertV2.dto.NoticeDto;
import com.ZeroWaveV2.FireAlertV2.model.Notice;
import com.ZeroWaveV2.FireAlertV2.repository.NoticeRepository;

@Service
public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;
    
    @Value("${notice.image.storage}")
	private String imageStoragePath;

    public List<NoticeDto> findNoticeSummary() {
        return noticeRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }
    
    // 공지사항 생성(Create)
    public Notice createNotice(NoticeDto noticeDto, MultipartFile image) throws IOException {
        String imageName = saveImage(image);
        Notice notice = new Notice();
        notice.setTitle(noticeDto.getTitle());
        notice.setPost(noticeDto.getPost());
        notice.setImgurl(imageName);
        return noticeRepository.save(notice);
    }

    private String saveImage(MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            Path directoryPath = Paths.get(imageStoragePath);
            if (!Files.exists(directoryPath)) {
    			Files.createDirectories(directoryPath);
    		}
            String originalFilename = image.getOriginalFilename();
            String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf("."))
    				: "";
            String newFileName = UUID.randomUUID().toString() + fileExtension;
            //String newFileName = originalFilename + fileExtension;
            Path imagePath = directoryPath.resolve(newFileName);
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            
            return imagePath.toString();
        }
        return null;
    }
    
    // 공지사항 번호로 테이블 내에 있는 데이터 가져오기
    public NoticeDto getNoticeByNum(int num) {
    	Notice notice = noticeRepository.findByNum(num);
        if (notice != null) {
            return convertEntityToDto(notice);
        }
        return null;
    }

    private NoticeDto convertEntityToDto(Notice notice) {
    	NoticeDto noticeDTO = new NoticeDto();
        noticeDTO.setNum(notice.getNum());
        noticeDTO.setTitle(notice.getTitle());
        noticeDTO.setPost(notice.getPost());
        noticeDTO.setHit(notice.getHit());
        noticeDTO.setImgurl(notice.getImgurl());
        noticeDTO.setCreateDate(notice.getCreateDate());
        noticeDTO.setModifyDate(notice.getModifyDate());
        return noticeDTO;
    }
    
    // 수정
    public NoticeDto updateNotice(int num, NoticeDto noticeDto, MultipartFile image) throws IOException {
    	String imageName = saveImage(image);
        Notice existingNotice = noticeRepository.findByNum(num);
        if (existingNotice == null) {
            throw new RuntimeException("Notice not found for this num :: " + num);
        }
        existingNotice.setTitle(noticeDto.getTitle());
        existingNotice.setPost(noticeDto.getPost());
        existingNotice.setImgurl(imageName);
        Notice updatedNotice = noticeRepository.save(existingNotice);
        return convertEntityToDto(updatedNotice);
    }
    
    // 삭제
    public void deleteNotice(int num) {
        Notice notice = noticeRepository.findByNum(num);
        if (notice == null) {
            throw new RuntimeException("Notice not found for this num :: " + num);
        }
        noticeRepository.delete(notice);
    }
    
    // 조회 수 증가
    public void increaseNoticeHit(int num) {
    	Notice notice = noticeRepository.findByNum(num);
        notice.increaseHit();
        noticeRepository.save(notice); // 변경 감지를 통한 업데이트
    }    
       
    public Optional<Notice> findPreviousNotice(int currentNum) {
        return noticeRepository.findFirstByNumLessThanOrderByNumDesc(currentNum);
    }

    public Optional<Notice> findNextNotice(int currentNum) {
        return noticeRepository.findFirstByNumGreaterThanOrderByNumAsc(currentNum);
    }
}

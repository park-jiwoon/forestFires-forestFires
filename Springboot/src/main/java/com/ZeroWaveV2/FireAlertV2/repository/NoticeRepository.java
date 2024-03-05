package com.ZeroWaveV2.FireAlertV2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ZeroWaveV2.FireAlertV2.dto.NoticeDto;
import com.ZeroWaveV2.FireAlertV2.model.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    @Query("SELECT new com.ZeroWaveV2.FireAlertV2.dto.NoticeDto(n.num, n.title, n.createDate) FROM Notice n")
    List<NoticeDto> findNoticeSummary();
        
    Notice findByNum(int num);
    
    Optional<Notice> findFirstByNumLessThanOrderByNumDesc(int num);
    Optional<Notice> findFirstByNumGreaterThanOrderByNumAsc(int num);   
}

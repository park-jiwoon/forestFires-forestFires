package com.ZeroWaveV2.FireAlertV2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ZeroWaveV2.FireAlertV2.model.FireReception;

@Repository
public interface FireReceptionRepository extends JpaRepository<FireReception, Integer> {
    List<FireReception> findByUser_Hp(String hp); // 메서드 명 수정
    
    @Query("SELECT n.progress, COUNT(n) FROM FireReception n GROUP BY n.progress")
    List<Object[]> countByProgress();
    
    Optional<FireReception> findByNum(int num);
    
    List<FireReception> findByUser_HpOrderByAdateDesc(String hp);
}

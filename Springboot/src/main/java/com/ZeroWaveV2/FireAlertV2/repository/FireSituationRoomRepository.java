package com.ZeroWaveV2.FireAlertV2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ZeroWaveV2.FireAlertV2.model.FireSituationRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface FireSituationRoomRepository extends JpaRepository<FireSituationRoom, Integer> {
    
	// 특정 상태 값을 가진 FireSituationRoom 엔티티들을 조회하는 메소드
    List<FireSituationRoom> findByState(String state);
    
    List<FireSituationRoom> findByFireStationFs(String fs);
    
    Optional<FireSituationRoom> findByCommand(int command);
}

package com.ZeroWaveV2.FireAlertV2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ZeroWaveV2.FireAlertV2.dto.FireStationDto;
import com.ZeroWaveV2.FireAlertV2.model.FireStation;

@Repository
public interface FireStationRepository extends JpaRepository<FireStation, String> {
	FireStation findByFs(String fs);
	
	Optional<FireStation> findByFsAdd(String fsAdd);
	
	@Query("SELECT new com.ZeroWaveV2.FireAlertV2.dto.FireStationDto(f.fsName, f.fsAdd, f.fph) FROM FireStation f")
    List<FireStationDto> findFireStationSummary();
}

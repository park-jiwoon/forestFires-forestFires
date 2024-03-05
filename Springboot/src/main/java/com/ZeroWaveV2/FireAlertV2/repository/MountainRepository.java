package com.ZeroWaveV2.FireAlertV2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ZeroWaveV2.FireAlertV2.model.Mountain_recommend;

@Repository
public interface MountainRepository extends JpaRepository<Mountain_recommend, String> {
	@Query("SELECT DISTINCT m.address FROM Mountain_recommend m")
	List<String> findDistinctAddress();
	
	@Query("SELECT DISTINCT m.mttime FROM Mountain_recommend m")
	List<String> findDistinctMtTime();
	
	@Query("SELECT DISTINCT m.season FROM Mountain_recommend m")
	List<String> findDistinctSeason();
	
	@Query("SELECT m FROM Mountain_recommend m WHERE m.height >= :minHeight AND m.height <= :maxHeight")
    List<Mountain_recommend> findByHeightRange(@Param("minHeight") int minHeight, @Param("maxHeight") int maxHeight);
    
    @Query(value = "SELECT * FROM (SELECT m.* FROM mountain_recommend m WHERE m.season = :season ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM = 1", nativeQuery = true)
    List<Mountain_recommend> findRandomBySeason(@Param("season") String season);

}

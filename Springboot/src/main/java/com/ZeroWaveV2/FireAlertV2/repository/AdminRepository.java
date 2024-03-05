package com.ZeroWaveV2.FireAlertV2.repository;

import com.ZeroWaveV2.FireAlertV2.model.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
	//Admin findById(String id);	
}

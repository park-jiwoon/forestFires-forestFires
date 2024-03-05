package com.ZeroWaveV2.FireAlertV2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ZeroWaveV2.FireAlertV2.jwt.JwtTokenProvider;
import com.ZeroWaveV2.FireAlertV2.model.Admin;
import com.ZeroWaveV2.FireAlertV2.repository.AdminRepository;

@Service
public class AdminService {
	
	private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    public AdminService(AdminRepository adminRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

	// 로그인
    public Admin authenticate(String id, String password) {
    	return adminRepository.findById(id)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(null);
    }
}

package com.ZeroWaveV2.FireAlertV2.config;

import com.ZeroWaveV2.FireAlertV2.model.Admin;
import com.ZeroWaveV2.FireAlertV2.repository.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

	private final BCryptPasswordEncoder passwordEncoder;

	// 생성자를 통한 BCryptPasswordEncoder 의존성 주입
	@Autowired
	public DataInitializer(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	CommandLineRunner initDatabase(AdminRepository repository) {
		return args -> {
			String encodedPassword = passwordEncoder.encode("1234");
			Admin admin = new Admin("system", encodedPassword);
			if (repository.findById(admin.getId()).isEmpty()) {
				repository.save(admin);
			}
		};
	}
	
	
}

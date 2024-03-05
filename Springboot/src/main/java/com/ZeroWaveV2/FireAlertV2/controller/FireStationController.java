package com.ZeroWaveV2.FireAlertV2.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
//240228 수정
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//240228 수정

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ZeroWaveV2.FireAlertV2.constants.ErrorMessages;
import com.ZeroWaveV2.FireAlertV2.dto.FireStationDto;
import com.ZeroWaveV2.FireAlertV2.jwt.JwtTokenProvider;

//240228 수정
import com.ZeroWaveV2.FireAlertV2.model.FireStation;
//240228 수정
import com.ZeroWaveV2.FireAlertV2.model.Member;
import com.ZeroWaveV2.FireAlertV2.repository.FireStationRepository;
import com.ZeroWaveV2.FireAlertV2.service.FireStationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/firestation")
public class FireStationController {

	@Autowired
	private FireStationRepository fireStationRepository;
	private final FireStationService fireStationService;
	private final JwtTokenProvider jwtTokenProvider;
	
	public FireStationController(FireStationService fireStationService, JwtTokenProvider jwtTokenProvider) {
        this.fireStationService = fireStationService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
	
	// 240228 수정
	// 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login_firestation(@Valid @RequestBody FireStationDto fireStationDto) {
        var firestation = fireStationService.fireStation_login(fireStationDto.getFs(), fireStationDto.getPassword());
        if (firestation != null) {
            // Set token validity to 1 hour
        	List<String> roles = List.of("ROLE_FIRETEAM");
            long validityInMilliseconds = 3600000;
            var jwtToken = jwtTokenProvider.createToken(firestation.getFs(), roles, firestation.getFs(), validityInMilliseconds);
            return ResponseEntity.ok(Map.of("token", jwtToken));
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", ErrorMessages.AUTHENTICATION_FAILED));
        }
    }
    
    // 240228 수정
    // fs 값을 받아 해당 FireStation 정보를 조회하는 API
    @GetMapping("/{fs}")
    public ResponseEntity<?> getFireStationByFs(@PathVariable String fs) {
        FireStation fireStation = fireStationService.findFireStationByFs(fs);
        if (fireStation != null) {
            // 조회 성공 시, 조회된 FireStation 정보 반환
            return ResponseEntity.ok(fireStation);
        } else {
            // 조회 실패 시, NOT FOUND 응답 반환
            return ResponseEntity.notFound().build();
        }
    }
    
    // 전체 소방서 조회
    @GetMapping
    public ResponseEntity<List<FireStationDto>> getAllFireStation() {
        List<FireStationDto> fireStation = fireStationService.findFireStationSummary();
        return ResponseEntity.ok(fireStation);
    }
    
    @GetMapping("/info")
    public ResponseEntity<?> getFireStationInfo(@AuthenticationPrincipal UserDetails currentUser) {
        FireStation fireStation = fireStationRepository.findByFs(currentUser.getUsername()); // Spring Security 기본 구현에서는 getUsername()이 사용자 식별자를 반환합니다.
        if (fireStation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Map<String, Object> fireStationInfo = new HashMap<>();
        fireStationInfo.put("fsName", fireStation.getFsName());        

        return ResponseEntity.ok(fireStationInfo);
    }
}

package com.ZeroWaveV2.FireAlertV2.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ZeroWaveV2.FireAlertV2.constants.ErrorMessages;
import com.ZeroWaveV2.FireAlertV2.dto.AdminDto;
import com.ZeroWaveV2.FireAlertV2.jwt.JwtTokenProvider;
import com.ZeroWaveV2.FireAlertV2.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminController(AdminService adminService, JwtTokenProvider jwtTokenProvider) {
        this.adminService = adminService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AdminDto adminDto) {
        var user = adminService.authenticate(adminDto.getId(), adminDto.getPassword());
        if (user != null) {
        	 List<String> roles = List.of("ROLE_ADMIN"); 
        	 long validityInMilliseconds = 3600000;
            var jwtToken = jwtTokenProvider.createToken(user.getId(), roles, validityInMilliseconds);
            return ResponseEntity.ok(Map.of("token", jwtToken));
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", ErrorMessages.AUTHENTICATION_FAILED));
        }
    }
}

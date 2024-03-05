package com.ZeroWaveV2.FireAlertV2.controller;

import com.ZeroWaveV2.FireAlertV2.dto.GuestDto;
import com.ZeroWaveV2.FireAlertV2.jwt.JwtTokenProvider;
import com.ZeroWaveV2.FireAlertV2.model.Guest;
import com.ZeroWaveV2.FireAlertV2.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
//import javax.validation.Valid;
@RestController
@RequestMapping("/api/guest")
public class GuestController {

    private final GuestService guestService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public GuestController(GuestService guestService, JwtTokenProvider tokenProvider) {
        this.guestService = guestService;
        this.jwtTokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginOrRegisterGuest(@Validated @RequestBody GuestDto guestLoginDto) {
        String hp = guestLoginDto.getHp();
        if (hp.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "전화번호를 입력하세요."));
        }

        Guest guest = guestService.createOrFindGuest(hp);
        if (guest != null) {
        	long validityInMilliseconds = 3600000;
        	List<String> roles = List.of("ROLE_GUEST");
			var jwtToken = jwtTokenProvider.createToken(guest.getHp(), roles, validityInMilliseconds);
            return ResponseEntity.ok(Map.of("message", "비회원 로그인 성공", "token", jwtToken));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "비회원 로그인 실패"));
        }
    }
}


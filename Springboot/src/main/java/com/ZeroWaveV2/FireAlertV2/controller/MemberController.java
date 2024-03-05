package com.ZeroWaveV2.FireAlertV2.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ZeroWaveV2.FireAlertV2.constants.ErrorMessages;
import com.ZeroWaveV2.FireAlertV2.dto.ChangPasswordDto;
import com.ZeroWaveV2.FireAlertV2.dto.LoginDto;
import com.ZeroWaveV2.FireAlertV2.dto.MemberDto;
import com.ZeroWaveV2.FireAlertV2.dto.UserRegistrationDto;
import com.ZeroWaveV2.FireAlertV2.jwt.JwtTokenProvider;
import com.ZeroWaveV2.FireAlertV2.repository.MemberRepository;
import com.ZeroWaveV2.FireAlertV2.service.MemberService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
public class MemberController {

	@Autowired
    private final MemberService memberService;
	
	
    private final JwtTokenProvider jwtTokenProvider;

    public MemberController(MemberRepository memberRepository, MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 연락처 중복 검사
    @PostMapping("/check_hp")
    public ResponseEntity<?> checkId(@RequestBody Map<String, String> data) {
        String hp = data.get("hp");
        if (hp == null || hp.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "연락처를 입력하세요."));
        }

        if (memberService.existsByHp(hp)) {
            return ResponseEntity.ok(Map.of("message", "이미 존재하는 연락처입니다.", "exists", true));
        } else {
            return ResponseEntity.ok(Map.of("message", "사용 가능한 연락처입니다.", "exists", false));
        }
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        var user = memberService.registerUser(
        		registrationDto.getHp(),
                registrationDto.getPassword(),
                registrationDto.getUserName());

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", ErrorMessages.USER_ALREADY_EXISTS));
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        var user = memberService.authenticate(loginDto.getHp(), loginDto.getPassword());
        if (user != null) {
        	List<String> roles = List.of("ROLE_USER");
        	long validityInMilliseconds = 3600000;
            var jwtToken = jwtTokenProvider.createToken(user.getHp(), roles, validityInMilliseconds);
            return ResponseEntity.ok(Map.of("token", jwtToken));
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", ErrorMessages.AUTHENTICATION_FAILED));
        }
    }
    
    // 비밀번호 찾기
    @PutMapping("/find-password")
    public ResponseEntity<?> verifyAndChangePassword(@RequestBody ChangPasswordDto request) {
        if (memberService.verifyAndChangePassword(request.getHp(), request.getUsername(), request.getPassword())) {
            return ResponseEntity.ok().body("비밀번호 변경에 성공했습니다.");
        } else {
            return ResponseEntity.badRequest().body("사용자 검증 실패 또는 비밀번호 변경에 실패했습니다.");
        }
    }
    
    // 관리자용 모든 회원 조회
    @GetMapping("/allmembers")
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        List<MemberDto> member = memberService.findMemberSummary();
        return ResponseEntity.ok(member);
    }
    
    // 관리자용 회원 삭제
    @DeleteMapping("/delete/{hp}")
    public ResponseEntity<?> deleteUserByHp(@PathVariable String hp) {
        boolean isDeleted = memberService.deleteUserByHp(hp);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }    
}

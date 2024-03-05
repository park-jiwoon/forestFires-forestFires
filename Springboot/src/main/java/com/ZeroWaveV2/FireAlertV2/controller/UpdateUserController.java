package com.ZeroWaveV2.FireAlertV2.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ZeroWaveV2.FireAlertV2.dto.UpdateUserDto;
import com.ZeroWaveV2.FireAlertV2.model.Member;
import com.ZeroWaveV2.FireAlertV2.repository.MemberRepository;
import com.ZeroWaveV2.FireAlertV2.service.MemberService;


@RestController
@RequestMapping("/api/mypage")
public class UpdateUserController {

    @Autowired
    private MemberRepository memberRepository;
    
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails currentUser) {
        Member user = memberRepository.findByHp(currentUser.getUsername()); // Spring Security 기본 구현에서는 getUsername()이 사용자 식별자를 반환합니다.
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", user.getUserName());
        userInfo.put("hp", user.getHp());

        return ResponseEntity.ok(userInfo);
    }
    
    @Autowired
    private MemberService memberService;
    
    @PutMapping("/update_pw")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails currentUser, @RequestBody UpdateUserDto updateUserDto) {
        boolean isChanged = memberService.changePassword(currentUser.getUsername(), updateUserDto.getCurrentPassword(), updateUserDto.getNewPassword());

        if (isChanged) {
            return ResponseEntity.ok().body(Map.of("success",true,"message", "비밀번호가 성공적으로 변경되었습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("fail",false,"message", "비밀번호 변경에 실패했습니다. 기존 비밀번호를 확인해주세요."));
        }
    }
}

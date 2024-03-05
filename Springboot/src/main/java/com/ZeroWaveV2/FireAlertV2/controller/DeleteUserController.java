package com.ZeroWaveV2.FireAlertV2.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ZeroWaveV2.FireAlertV2.service.MemberService;

@RestController
@RequestMapping("/api/mypage")
public class DeleteUserController {

    @Autowired
    private MemberService memberService;

    @DeleteMapping("/delete_account")
    public ResponseEntity<?> deleteUserAccount(@AuthenticationPrincipal UserDetails currentUser) {
        boolean isDeleted = memberService.deleteUserByHp(currentUser.getUsername());

        if (isDeleted) {
            return ResponseEntity.ok().body(Map.of("message", "계정이 성공적으로 삭제되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "계정을 찾을 수 없습니다."));
        }
    }
}

package com.ZeroWaveV2.FireAlertV2.service;

import com.ZeroWaveV2.FireAlertV2.model.Member;
import com.ZeroWaveV2.FireAlertV2.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ZeroWaveV2.FireAlertV2.dto.MemberDto;
import com.ZeroWaveV2.FireAlertV2.jwt.JwtTokenProvider;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 아이디 중복 확인
    public boolean existsByHp(String hp) {
        return memberRepository.existsByHp(hp);
    }

    // 회원가입
    public Member registerUser(String hp, String password, String userName) {
        if (memberRepository.findByHp(hp) != null) {
            // 사용자가 이미 존재하면 null을 반환
            return null;
        }

        // 사용자가 존재하지 않으면 새로운 User 객체를 생성하고 저장
        String encodedPassword = passwordEncoder.encode(password);
        Member newUser = new Member(hp, encodedPassword, userName);
        return memberRepository.save(newUser);
    }

    // 로그인
    public Member authenticate(String hp, String password) {
        Member user = memberRepository.findByHp(hp);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
    
    // 회원정보 가져오기
    public Member info(String hp) {
       Member user = memberRepository.findByHp(hp);
        return user;
    }
    
    //비밀번호 수정
    public boolean changePassword(String hp, String currentPassword, String newPassword) {
        Member user = memberRepository.findByHp(hp);
        if (user != null && passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            memberRepository.save(user);
            return true;
        }
        return false;
    }
    
    //회원 삭제
    public boolean deleteUserByHp(String hp) {
        Member user = memberRepository.findByHp(hp);
        if (user != null) {
           memberRepository.delete(user);
            return true;
        }
        return false;
    }
    
    //비밀번호 변경
    public boolean verifyAndChangePassword(String hp, String username, String newPassword) {
        return memberRepository.findByHpAndUserName(hp, username)
            .map(user -> {
            	user.setPassword(passwordEncoder.encode(newPassword));
                memberRepository.save(user);
                return true;
            })
            .orElse(false);
    }
    
    //관리자용 모든 회원 정보 조회
    public List<MemberDto> findMemberSummary() {
        return memberRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }
    
    private MemberDto convertEntityToDto(Member member) {
    	MemberDto memberDTO = new MemberDto();
    	memberDTO.setHp(member.getHp());
    	memberDTO.setUserName(member.getUserName());
    	memberDTO.setCreateDate(member.getCreateDate());
        
        return memberDTO;
    }
    
    public void validateJwt(String jwtToken) throws Exception {
      jwtTokenProvider.validateToken(jwtToken);
   }
    
}
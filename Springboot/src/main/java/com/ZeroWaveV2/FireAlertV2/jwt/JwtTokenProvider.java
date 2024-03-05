package com.ZeroWaveV2.FireAlertV2.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@PostConstruct
	public void init() {
		if (jwtSecret.length() < 32) {
			var key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
			jwtSecret = Base64.getEncoder().encodeToString(key.getEncoded());
		}
	}

	// 토큰 생성
	public String createToken(String hp, List<String> roles, long validityInMilliseconds) {
		var claims = Jwts.claims().setSubject(hp);
		claims.put("roles", roles);
		Date now = new Date();
		Date validity = new Date(now.getTime() + 3600000); // 1시간 후 만료

		return Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE).setClaims(claims).setIssuedAt(now)
				.setExpiration(validity) // 만료 시간 설정
				.signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256).compact();
	}
	
	// 240228 수정
	// 토큰 생성 오버로딩 (fs)
	public String createToken(String subject, List<String> roles, String fs, long validityInMilliseconds) {
	    Claims claims = Jwts.claims().setSubject(subject);
	    claims.put("roles", roles);
	    if (fs != null && !fs.isEmpty()) {
	        claims.put("fs", fs); // fs 값을 claims에 추가
	    }

	    Date now = new Date();
	    Date validity = new Date(now.getTime() + validityInMilliseconds);

	    return Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE).setClaims(claims).setIssuedAt(now)
				.setExpiration(validity) // 만료 시간 설정
				.signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256).compact();
	}


	// 토큰 검증
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes()).build().parseClaimsJws(token);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			throw new RuntimeException("The token is expired", e);
		} catch (SecurityException e) {
			throw new RuntimeException(
					"Invalid JWT signature. Please check if the jwtSecret matches the one used for token creation.", e);
		} catch (Exception e) {
			throw new RuntimeException("Invalid JWT token", e);
		}
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = this.getUserDetails(token);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private UserDetails getUserDetails(String token) {
		String userId = Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes()).build().parseClaimsJws(token).getBody()
				.getSubject();
		return new User(userId, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
	}

	// 요청으로부터 JWT 토큰을 추출하는 메서드
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7); // "Bearer " 이후의 토큰 부분만 반환
		}
		return null;
	}

	// 토큰에서 역할 정보를 추출하는 메서드 추가
	public List<String> getRolesFromToken(String token) {
		Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build()
				.parseClaimsJws(token);
		Claims claims = claimsJws.getBody();

		// 토큰 생성 시 사용한 'roles' 키를 이용하여 역할 정보를 추출
		List<String> roles = claims.get("roles", List.class);

		// 추출한 역할 정보 반환
		if (roles == null) {
			return new ArrayList<>();
		}
		return roles;
	}

}
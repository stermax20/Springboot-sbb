package com.mysite.sbb.jwt;

import java.time.Duration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService {
	private final UserService userService;
	private final TokenProvider tokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final JwtProperties jwtProperties;
	private final RefreshTokenRepository refreshTokenRepository;

	public AccessTokenResponse getAccessToken(AccessTokenRequest request) {
		SiteUser user = userService.getUser(request.getUsername());
		if (user != null) {
			if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
				return createAccessToken(user, null);
			}
		}
		return null;
	}
	
	private AccessTokenResponse createAccessToken(SiteUser user, String refreshToken){
		Duration tokenDuration = Duration.ofMinutes(jwtProperties.getDuration());
		Duration refreshDuration = Duration.ofMinutes(jwtProperties.getRefreshDuration());

		// refreshToken 검색
		RefreshToken savedRefreshToken = refreshTokenRepository.findByUsername(user.getUsername()).orElse(null);

		if(savedRefreshToken !=null && refreshToken != null){
			// 전달 받은 리프레시 토큰이 사용자에게 저장된 토큰과 다르다면
			if(!savedRefreshToken.getRefreshToken().equals(refreshToken))
				return new AccessTokenResponse("Invalid token.", null, null);
		}

		String accessToken = tokenProvider.generateToken(user, tokenDuration, true);
		String newRefreshToken = tokenProvider.generateToken(user, refreshDuration, false);

		if(savedRefreshToken == null)
			savedRefreshToken = new RefreshToken(user.getUsername(), newRefreshToken);
		else
			savedRefreshToken.setRefreshToken(newRefreshToken);
		refreshTokenRepository.save(savedRefreshToken);
		return new AccessTokenResponse("ok", accessToken, newRefreshToken);
	}
	
	public AccessTokenResponse refreshAccessToken(CreateAccessTokenByRefreshToken request){
		try {
			Claims claims = tokenProvider.getClaims(request.getRefreshToken());
			String type = claims.get("type").toString();
			if (type == null || !type.equals("R")) {
				throw new Exception("Invalid token");
			}

			SiteUser user = userService.getUser(claims.getSubject());
			return createAccessToken(user, request.getRefreshToken());

		} catch(ExpiredJwtException e){
			return new AccessTokenResponse("만료된 토큰", null, null);
		}catch (Exception e){
			System.out.println(e.getMessage());
			return new AccessTokenResponse(e.getMessage(), null, null);
		}
	}
}

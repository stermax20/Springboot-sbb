package com.mysite.sbb.jwt;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mysite.sbb.user.SiteUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenProvider {

	private final JwtProperties jwtProperties;
	private final SecretKey key;
	private final JwtParser parser;

	public TokenProvider(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProperties.getSecretKey()));
		parser = Jwts.parser().verifyWith(key).build();
	}

	public String generateToken(SiteUser siteUser, Duration expiredAt, boolean isAccessToken){
		Date now = new Date();
		Date expiry = new Date(now.getTime()+expiredAt.toMillis());

		return Jwts.builder()
				.header().add("type", "JWT").add("alg", "HS256").and()
				.claims()
					.issuer(jwtProperties.getIssuer())
					.issuedAt(now)
					.expiration(expiry)
					.subject(siteUser.getUsername())
					.add("type", isAccessToken? "A":"R")
					.add("id", siteUser.getId())
					.and()
				.signWith(key, Jwts.SIG.HS256)
				.compact();
	}

	public Authentication getAuthentication(String token){
		Claims claims = getClaims(token);

		// RefreshToken은 사용할 수 없다.
		String type = claims.get("type").toString();
		if(type==null || !claims.get("type").equals("A")) throw new IllegalArgumentException("");

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("user"));

		UserDetails userDetails = org.springframework.security.core.userdetails.User
				.withUsername(claims.getSubject()) // email
				.password("")
				.authorities(authorities)
				.build();

		return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
	}

	public Claims getClaims(String token) {
		Jws<Claims> jws = parser.parseSignedClaims(token);
		return jws.getPayload();
	}
}
package com.mysite.sbb.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccessTokenResponse {
	private String result;
	private String token;
	private String refreshToken;
}

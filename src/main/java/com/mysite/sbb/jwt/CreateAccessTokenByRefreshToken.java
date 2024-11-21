package com.mysite.sbb.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenByRefreshToken {
	private String refreshToken;
}
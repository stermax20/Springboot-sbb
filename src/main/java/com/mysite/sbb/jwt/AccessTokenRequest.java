package com.mysite.sbb.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenRequest {
	String username;
	String password;
}

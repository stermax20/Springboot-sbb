package com.mysite.sbb.api;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mysite.sbb.jwt.AccessTokenRequest;
import com.mysite.sbb.jwt.AccessTokenResponse;
import com.mysite.sbb.jwt.CreateAccessTokenByRefreshToken;
import com.mysite.sbb.jwt.TokenService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

	private final TokenService tokenService;
	private final QuestionService questionService;

	@PostMapping("/login")
	public ResponseEntity<AccessTokenResponse> login(
			@RequestBody AccessTokenRequest request
	){
	  AccessTokenResponse token = tokenService.getAccessToken(request);
		if(token != null) {
		  // 일단 AccessToken만 발행
			return ResponseEntity.ok().body(token);
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@PostMapping("/login/token")
	public ResponseEntity<AccessTokenResponse> tokenLogin(
			@RequestBody CreateAccessTokenByRefreshToken request
	){
		AccessTokenResponse response = tokenService.refreshAccessToken(request);
		if(response != null)
			return ResponseEntity.ok().body(response);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@GetMapping("/questions")
	public ResponseEntity<Page<Question>> questions(

	@RequestParam(value="page", defaultValue="0") int page,
	@RequestParam(value="keyword", defaultValue="")String keyword

	){
	return ResponseEntity.ok(questionService.getList(page, keyword));
	}

}
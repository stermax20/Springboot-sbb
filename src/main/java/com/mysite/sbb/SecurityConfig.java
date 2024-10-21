package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/* Security 예외 처리. */
	@Bean
	WebSecurityCustomizer configure() {
		return web -> web.ignoring().requestMatchers(toH2Console())
				.requestMatchers(
						new AntPathRequestMatcher("/css/**"),
						new AntPathRequestMatcher("/js/**"));
	}

	/* 주소별 보안 적용 정책 정의 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests(auth -> auth
			 .requestMatchers(
				  new AntPathRequestMatcher("/user/login"),
				  new AntPathRequestMatcher("/user/signup"),
				  new AntPathRequestMatcher("/question/list"),
				  new AntPathRequestMatcher("/question/detail/**"),
				  new AntPathRequestMatcher("/"))
			 .permitAll() // 위에서 정의한 주소들은 보안 적용하지 않고
			 .anyRequest()// 위 주소를 제외한 모든 주소
			 .authenticated()) //보안 적용
		.formLogin(login -> login
				.loginPage("/user/login")
				.defaultSuccessUrl("/"))
		.logout(logout-> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // method 상관 없음
				//.logoutUrl("/user/logout") // CSRF on 시 POST method 만 가능
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true));
		
		return http.build();
	}

	@Bean
	PasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
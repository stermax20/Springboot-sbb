package com.mysite.sbb.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public SiteUser create(String username, String email, String password) {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setEmail(email);
		// 비밀번호는 암호화하여 저장
		user.setPassword(passwordEncoder.encode(password));
		return userRepository.save(user);
	}

	public SiteUser getUser(String username) {
		SiteUser user = userRepository.findByUsername(username)

				.orElseThrow(() -> new DataNotFoundException("User not found"));

		return user;
	}
}
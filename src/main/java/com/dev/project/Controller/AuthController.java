package com.dev.project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.project.DTO.LoginDTO;
import com.dev.project.Repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final UserRepository userRepository;

	@Autowired
	public AuthController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginRequest) {
		var user = userRepository.findByName(loginRequest.getName());
		if (user.isPresent() && BCrypt.checkpw(loginRequest.getPassword(), user.get().getPassword())) {
			// Generate JWT or session token
			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(user.get(), null, null);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			SecurityContextHolder.setContext(SecurityContextHolder.getContext());
			System.out.println(SecurityContextHolder.getContext().getAuthentication());

			return ResponseEntity.ok("Login successful");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	}
}

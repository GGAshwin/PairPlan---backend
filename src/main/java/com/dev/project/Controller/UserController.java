package com.dev.project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.project.DTO.CreateUserResponse;
import com.dev.project.Entity.UserEntity;
import com.dev.project.Repository.UserRepository;
import com.dev.project.Repository.WorkspaceRepository;
import com.dev.project.Service.WorkspaceService;
import org.springframework.security.crypto.bcrypt.BCrypt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "User management operations")
public class UserController {
	private UserRepository userRepository;
	private WorkspaceRepository workspaceRepository;
	private WorkspaceService workspaceService;

	@Autowired
	public UserController(UserRepository userRepository,
			WorkspaceRepository workspaceRepository,
			WorkspaceService workspaceService) {
		this.userRepository = userRepository;
		this.workspaceRepository = workspaceRepository;
		this.workspaceService = workspaceService;

	}

	// Create a new user
	@PostMapping
	@Operation(summary = "Create a new user", description = "Register a new user account with encrypted password", responses = {
			@ApiResponse(responseCode = "201", description = "User created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid user data")
	})
	public ResponseEntity<CreateUserResponse> createUser(@RequestBody UserEntity userRequest) {
		// Derive workspace details from the user details, save the workspace details
		// then save the user details
		String encryptedPassword = BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt());
		userRequest.setPassword(encryptedPassword);

		userRepository.save(userRequest);

		CreateUserResponse response = new CreateUserResponse(userRequest.getId(), "User created successfully");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// // only for testing, remove after using
	// @DeleteMapping("/everything")
	// public String deleteEverything() {
	// 	workspaceRepository.deleteAll();
	// 	userRepository.deleteAll();

	// 	return "Deleted!!!";
	// }
}

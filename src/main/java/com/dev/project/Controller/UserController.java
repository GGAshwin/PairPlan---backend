package com.dev.project.Controller;

import java.net.http.HttpResponse;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.project.DTO.CreateUserResponse;
import com.dev.project.DTO.JoinDTO;
import com.dev.project.Entity.UserEntity;
import com.dev.project.Entity.WorkspaceEntity;
import com.dev.project.Repository.ChecklistRepository;
import com.dev.project.Repository.UserRepository;
import com.dev.project.Repository.WorkspaceRepository;
import com.dev.project.Service.WorkspaceService;
import org.springframework.security.crypto.bcrypt.BCrypt;


@RestController
@RequestMapping("/users")
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
	public ResponseEntity<CreateUserResponse> createUser(@RequestBody UserEntity userRequest) {
		// Derive workspace details from the user details, save the workspace details then save the user details
		String encryptedPassword = BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt());
		userRequest.setPassword(encryptedPassword);

		userRepository.save(userRequest);

		CreateUserResponse response = new CreateUserResponse(userRequest.getId(), "User created successfully");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	//only for testing, remove after using
	@DeleteMapping("/everything")
	public String deleteEverything(){
		workspaceRepository.deleteAll();
		userRepository.deleteAll();

		return "Deleted!!!";
	}
}

package com.dev.project.Controller;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.project.Component.JwtUtil;
import com.dev.project.DTO.JoinDTO;
import com.dev.project.DTO.UserResponseDTO;
import com.dev.project.DTO.WorkspaceCreateDTO;
import com.dev.project.DTO.WorkspaceResponseDTO;
import com.dev.project.Entity.UserEntity;
import com.dev.project.Entity.WorkspaceEntity;
import com.dev.project.Repository.UserRepository;
import com.dev.project.Repository.WorkspaceRepository;
import com.dev.project.Service.WorkspaceService;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceController {
	 private WorkspaceRepository workspaceRepository;
	 private WorkspaceService workspaceService;
	 private UserRepository userRepository;
	 private JwtUtil jwtUtil;

	 @Autowired
	public WorkspaceController( WorkspaceRepository workspaceRepository,
							   WorkspaceService workspaceService, UserRepository userRepository, JwtUtil jwtUtil){
		 this.workspaceRepository = workspaceRepository;
		 this.workspaceService = workspaceService;
		 this.userRepository = userRepository;
		 this.jwtUtil = jwtUtil;
	 }

	@PostMapping
	public ResponseEntity<Object> createWorkspace(@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.replace("Bearer ", "");
		String userName;

		try {
			userName = jwtUtil.extractName(token);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token!");
		}

		var userOptional = userRepository.findByName(userName);
		if (userOptional.isPresent()) {
			var validUser = userOptional.get();
			if (validUser.getWorkspace() != null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already has a workspace!");
			}

			WorkspaceEntity workspaceEntity = workspaceService.createWorkspace(validUser);
			if (workspaceEntity.getUsers() == null) {
				workspaceEntity.setUsers(new ArrayList<>());
			}
			workspaceEntity.getUsers().add(validUser);
			workspaceRepository.save(workspaceEntity);

			validUser.setWorkspace(workspaceEntity);
			userRepository.save(validUser);

			WorkspaceResponseDTO workspaceResponseDTO = WorkspaceResponseDTO.builder()
					.id(workspaceEntity.getId())
					.name(workspaceEntity.getName())
					.users(workspaceEntity.getUsers().stream()
							.map(user -> UserResponseDTO.builder()
									.id(user.getId())
									.name(user.getName())
									.createdChecklists(user.getCreatedChecklists())
									.build())
							.toList())
					.joinCode(workspaceEntity.getJoinCode())
					.createdAt(workspaceEntity.getCreatedAt())
					.build();

			return ResponseEntity.status(HttpStatus.CREATED).body(workspaceResponseDTO);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!!");
	}

	@PostMapping("/join")
	public ResponseEntity<String> joinWorkspace(@RequestBody JoinDTO joinRequest,
												@RequestHeader("Authorization") String authHeader
												) {
		// Null check for input validation
		if (joinRequest.getJoinCode() == null || joinRequest.getJoinCode().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad input, try again!!");
		}

		String token = authHeader.replace("Bearer ", "");
		String userName;

		try {
			userName = jwtUtil.extractName(token); // Assume jwtService is a service to handle JWT operations
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token!");
		}

		// Delegate the logic to the service
		String responseMessage = workspaceService.joinWorkspace(joinRequest, userRepository, userName,
				workspaceRepository);
		return switch (responseMessage) {
			case "Joined Workspace Successfully" -> ResponseEntity.status(HttpStatus.OK).body(responseMessage);
			case "Join Code not found!" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
			case "User not found!" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
			case "User already has a workspace!" -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
			default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred!");
		};
	}

	@GetMapping("/{workspaceId}")
	public ResponseEntity<Object> getWorkspaceById(@PathVariable UUID workspaceId,
												  @RequestHeader("Authorization") String authHeader) {
		String token = authHeader.replace("Bearer ", "");
		String userName;

		try {
			userName = jwtUtil.extractName(token);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token!");
		}

		var userOptional = userRepository.findByName(userName);
		if (userOptional.isPresent()) {
			var user = userOptional.get();
			var workspaceOptional = workspaceRepository.findById(workspaceId);

			if (workspaceOptional.isPresent()) {
				var workspace = workspaceOptional.get();
				if (workspace.getUsers().contains(user)) {
					WorkspaceResponseDTO workspaceResponseDTO = WorkspaceResponseDTO.builder()
							.id(workspace.getId())
							.name(workspace.getName())
							.joinCode(workspace.getJoinCode())
							.createdAt(workspace.getCreatedAt())
							.users(workspace.getUsers().stream()
									.map(u -> UserResponseDTO.builder()
											.id(u.getId())
											.name(u.getName())
											.createdChecklists(u.getCreatedChecklists())
											.build())
									.toList())
							.build();
					return ResponseEntity.ok(workspaceResponseDTO);
				} else {
					return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not part of this workspace!");
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Workspace not found!");
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!!");

	}
}

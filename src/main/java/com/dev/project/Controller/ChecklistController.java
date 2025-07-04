package com.dev.project.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.project.Component.JwtUtil;
import com.dev.project.DTO.ChecklistCreateDTO;
import com.dev.project.Entity.ChecklistEntity;
import com.dev.project.Entity.UserEntity;
import com.dev.project.Repository.ChecklistRepository;
import com.dev.project.Repository.UserRepository;
import com.dev.project.Repository.WorkspaceRepository;
import com.dev.project.Service.ChecklistService;

@RestController
@RequestMapping("/checklists")
public class ChecklistController {
	//dep injection
	private final ChecklistService checklistService;
	private final WorkspaceRepository workspaceRepository;
	private final UserRepository userRepository;
	private final ChecklistRepository checklistRepository;
	private final JwtUtil jwtUtil;

	@Autowired
	public ChecklistController(ChecklistService checklistService, WorkspaceRepository workspaceRepository,
							   UserRepository userRepository, ChecklistRepository checklistRepository, JwtUtil jwtUtil){
		this.checklistService = checklistService;
		this.workspaceRepository = workspaceRepository;
		this.userRepository = userRepository;
		this.checklistRepository = checklistRepository;
		this.jwtUtil = jwtUtil;
	}

	@GetMapping("")
	public List<ChecklistCreateDTO> getMyChecklists(@RequestHeader("Authorization") String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new RuntimeException("Missing or invalid Authorization header");
		}


		String token = authHeader.substring(7); // Strip "Bearer "
		System.out.println(token);

		String username = jwtUtil.extractName(token);
		System.out.println(username);
		if (!jwtUtil.isTokenValid(token, username)) {
			throw new RuntimeException("Invalid or expired token");
		}

		// Fetch user by name
		UserEntity user = userRepository.findByName(username)
				.orElseThrow(() -> new RuntimeException("User not found"));


		System.out.println(user);

		return checklistService.getMyChecklists(user, checklistRepository);

	}




	@GetMapping("/test")
	public String testSecurityContext() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);
		return "OK";
	}

	@PostMapping("")
	public ChecklistEntity createChecklist(@RequestHeader("Authorization") String authHeader,
							 @RequestBody ChecklistCreateDTO checkListRequest) {
		// Validate Authorization header
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new RuntimeException("Missing or invalid Authorization header");
		}

		// Extract and validate JWT token
		String token = authHeader.substring(7); // Strip "Bearer "
		String username = jwtUtil.extractName(token);
		if (!jwtUtil.isTokenValid(token, username)) {
			throw new RuntimeException("Invalid or expired token");
		}

		UserEntity user = userRepository.findByName(username)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// Logic to create a checklist
		return checklistService.createChecklist(checkListRequest, user,  workspaceRepository,
				checklistRepository);
	}

	@DeleteMapping("")
	public String deleteChecklist(@RequestHeader("Authorization") String authHeader, @RequestBody ChecklistCreateDTO checkListRequest) {

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new RuntimeException("Missing or invalid Authorization header");
		}

		// Extract and validate JWT token
		String token = authHeader.substring(7); // Strip "Bearer "
		String username = jwtUtil.extractName(token);
		if (!jwtUtil.isTokenValid(token, username)) {
			throw new RuntimeException("Invalid or expired token");
		}

		UserEntity user = userRepository.findByName(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		// Logic to delete a checklist
		 checklistService.deleteChecklist(checkListRequest, user, checklistRepository);
		return "Checklist deleted successfully";
	}
}

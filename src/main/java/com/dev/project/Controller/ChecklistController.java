package com.dev.project.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.project.Component.JwtUtil;
import com.dev.project.DTO.ChecklistCreateDTO;
import com.dev.project.DTO.ChecklistCreateRequestDTO;
import com.dev.project.Entity.ChecklistEntity;
import com.dev.project.Entity.UserEntity;
import com.dev.project.Repository.ChecklistRepository;
import com.dev.project.Repository.UserRepository;
import com.dev.project.Repository.WorkspaceRepository;
import com.dev.project.Service.ChecklistService;
import com.dev.project.Service.UserValidityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/checklists")
@Tag(name = "Checklist", description = "Checklist management operations")
@SecurityRequirement(name = "Bearer Authentication")
public class ChecklistController {
	// dep injection
	private final ChecklistService checklistService;
	private final WorkspaceRepository workspaceRepository;
	private final UserRepository userRepository;
	private final ChecklistRepository checklistRepository;
	private final UserValidityService userValidityService;

	@Autowired
	public ChecklistController(ChecklistService checklistService, WorkspaceRepository workspaceRepository,
			UserRepository userRepository, ChecklistRepository checklistRepository, UserValidityService userValidityService) {
		this.checklistService = checklistService;
		this.workspaceRepository = workspaceRepository;
		this.userRepository = userRepository;
		this.checklistRepository = checklistRepository;
		this.userValidityService = userValidityService;
	}

	@GetMapping("")
	@Operation(summary = "Get all checklists for authenticated user", description = "Retrieves all checklists from the user's workspace", responses = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved checklists"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - Invalid JWT token"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	public List<ChecklistCreateDTO> getMyChecklists(HttpServletRequest request) {
		UserEntity user = userValidityService.isUserExists(request);
		return checklistService.getMyChecklists(user, checklistRepository);
	}

	@PostMapping("")
	@Operation(summary = "Create a new checklist", description = "Creates a new checklist in the user's workspace", responses = {
			@ApiResponse(responseCode = "200", description = "Checklist created successfully"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - Invalid JWT token"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	public ChecklistEntity createChecklist(HttpServletRequest request,
			@RequestBody ChecklistCreateRequestDTO checkListRequest) {
		// Validate Authorization header
		UserEntity user = userValidityService.isUserExists(request);

		// Logic to create a checklist
		return checklistService.createChecklist(checkListRequest, user, workspaceRepository,
				checklistRepository);
	}

	@DeleteMapping("")
	@Operation(summary = "Delete a checklist", description = "Deletes a checklist from the user's workspace", responses = {
			@ApiResponse(responseCode = "200", description = "Checklist deleted successfully"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - Invalid JWT token"),
			@ApiResponse(responseCode = "404", description = "User or checklist not found")
	})
	public String deleteChecklist(HttpServletRequest request,
			@RequestBody ChecklistCreateDTO checkListRequest) {
			// Here i will have to send the checklistId as well as i am deleting the checklist
		UserEntity user = userValidityService.isUserExists(request);

		// Logic to delete a checklist
		checklistService.deleteChecklist(checkListRequest, user, checklistRepository);
		return "Checklist deleted successfully";
	}
}

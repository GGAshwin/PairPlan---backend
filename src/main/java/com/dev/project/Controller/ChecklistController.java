package com.dev.project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@Autowired
	public ChecklistController(ChecklistService checklistService, WorkspaceRepository workspaceRepository,
							   UserRepository userRepository, ChecklistRepository checklistRepository){
		this.checklistService = checklistService;
		this.workspaceRepository = workspaceRepository;
		this.userRepository = userRepository;
		this.checklistRepository = checklistRepository;
	}

	@GetMapping("")
	public ChecklistEntity getMyChecklists() {
		// Logic to get all checklists for the user
		// This method should be implemented in the ChecklistService
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);
		if (authentication == null || !authentication.isAuthenticated()) {
		System.out.println(authentication);
			throw new RuntimeException("User is not authenticated");
		}
		var user = (UserEntity) authentication.getPrincipal();

		return checklistService.getMyChecklists(user, checklistRepository);
	}

	@GetMapping("/test")
	public String testSecurityContext() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);
//		var user = (UserEntity) authentication.getPrincipal();
//		System.out.println(user);
//		return "Authenticated user: " + authentication.getPrincipal();
		return "OK";
	}

	@PostMapping("")
	public String createChecklist(@RequestBody ChecklistCreateDTO checkListRequest) {
		// Logic to create a checklist
		checklistService.createChecklist(checkListRequest, workspaceRepository, userRepository, checklistRepository);
		return "Checklist created successfully";
	}

	@DeleteMapping("")
	public String deleteChecklist(@RequestBody ChecklistCreateDTO checkListRequest) {
		// Logic to delete a checklist
		// This method should be implemented in the ChecklistService
		 checklistService.deleteChecklist(checkListRequest, checklistRepository);
		return "Checklist deleted successfully";
	}
}

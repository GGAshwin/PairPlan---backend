package com.dev.project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.project.DTO.ChecklistCreateDTO;
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

	@PostMapping("")
	public String createChecklist(@RequestBody ChecklistCreateDTO checkListRequest) {
		// Logic to create a checklist
		checklistService.createChecklist(checkListRequest, workspaceRepository, userRepository, checklistRepository);
		return "Checklist created successfully";
	}
}

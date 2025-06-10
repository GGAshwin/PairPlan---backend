package com.dev.project.Service;

import org.springframework.stereotype.Service;

import com.dev.project.DTO.ChecklistCreateDTO;
import com.dev.project.Repository.ChecklistRepository;
import com.dev.project.Repository.UserRepository;
import com.dev.project.Repository.WorkspaceRepository;

@Service
public class ChecklistService {
	public void createChecklist(ChecklistCreateDTO checkListRequest, WorkspaceRepository workspaceRepository,
								UserRepository userRepository, ChecklistRepository checklistRepository) {
		// Validate the request
		if (checkListRequest.getTitle() == null || checkListRequest.getTitle().isEmpty()) {
			throw new IllegalArgumentException("Checklist title cannot be empty");
		}
		if (checkListRequest.getWorkspaceId() == null) {
			throw new IllegalArgumentException("Workspace ID cannot be null");
		}
		if (checkListRequest.getCreatedById() == null) {
			throw new IllegalArgumentException("Created by ID cannot be null");
		}
		// Check if user exists
		var userExists = userRepository.findById(checkListRequest.getCreatedById());
		if (userExists.isEmpty()) {
			throw new IllegalArgumentException("User with ID " + checkListRequest.getCreatedById() + " does not exist");
		}
		// Check if workspace exists
		var workspace = workspaceRepository.findById(checkListRequest.getWorkspaceId());
		if(workspace.isEmpty()){
			throw new IllegalArgumentException("Workspace with ID " + checkListRequest.getWorkspaceId() + " does not exist");
		}
		// check if workspace is associated with user
		var usersArray = workspace.get().getUsers();
		var isUserAssocaited = usersArray.stream().anyMatch(user -> user.getId().equals(checkListRequest.getCreatedById()));
		System.out.println(isUserAssocaited);

		if (!isUserAssocaited) {
			throw new IllegalArgumentException("User with ID " + checkListRequest.getCreatedById() + " is not associated with the workspace");
		}
		// Create the checklist
		checklistRepository.save(checkListRequest.toChecklistEntity(workspace.get(), userExists.get()));
	}
}

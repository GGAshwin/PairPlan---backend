package com.dev.project.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dev.project.DTO.ChecklistCreateDTO;
import com.dev.project.Entity.ChecklistEntity;
import com.dev.project.Entity.UserEntity;
import com.dev.project.Repository.ChecklistRepository;
import com.dev.project.Repository.UserRepository;
import com.dev.project.Repository.WorkspaceRepository;

@Service
public class ChecklistService {
	public ChecklistEntity createChecklist(ChecklistCreateDTO checkListRequest,
			UserEntity user, WorkspaceRepository workspaceRepository, ChecklistRepository checklistRepository) {
		// check if name of checklist already exists in the workspace
		// if this throws an exception it means that the checklist already exists
		var existingChecklist = checklistRepository.findByTitleAndWorkspaceId(checkListRequest.getTitle(),
				checkListRequest.getWorkspaceId());
		if (existingChecklist.isPresent()) {
			throw new IllegalArgumentException("Checklist with title '" + checkListRequest.getTitle()
					+ "' already exists in the workspace with ID " + checkListRequest.getWorkspaceId());
		}

		// Validate the request
		if (checkListRequest.getTitle() == null || checkListRequest.getTitle().isEmpty()) {
			throw new IllegalArgumentException("Checklist title cannot be empty");
		}
		if (checkListRequest.getWorkspaceId() == null) {
			throw new IllegalArgumentException("Workspace ID cannot be null");
		}
		// Check if workspace exists
		var workspace = workspaceRepository.findById(checkListRequest.getWorkspaceId());
		if (workspace.isEmpty()) {
			throw new IllegalArgumentException(
					"Workspace with ID " + checkListRequest.getWorkspaceId() + " does not exist");
		}
		// check if workspace is associated with user
		var usersArray = workspace.get().getUsers();
		var isUserAssocaited = usersArray.stream().anyMatch(userItem -> userItem.getId().equals(user.getId()));
		System.out.println(isUserAssocaited);

		if (!isUserAssocaited) {
			throw new IllegalArgumentException("User with ID " + user.getId() + " is not associated with the" +
					" workspace");
		}
		// Create the checklist
		ChecklistEntity checklistEntity = checklistRepository.save(checkListRequest.toChecklistEntity(workspace.get(),
				user));

		return checklistEntity;
	}

	public void deleteChecklist(ChecklistCreateDTO checkListRequest,
			UserEntity user, ChecklistRepository checklistRepository) {
		// Validate the request
		if (checkListRequest.getWorkspaceId() == null) {
			throw new IllegalArgumentException("Workspace ID cannot be null");
		}
		if (checkListRequest.getTitle() == null || checkListRequest.getTitle().isEmpty()) {
			throw new IllegalArgumentException("Checklist title cannot be empty");
		}
		if (user.getId() == null) {
			throw new IllegalArgumentException("Created by ID cannot be null");
		}

		var checklistOptional = checklistRepository.findByTitleAndWorkspaceId(checkListRequest.getTitle(),
				checkListRequest.getWorkspaceId());

		if (checklistOptional.isEmpty()) {
			throw new IllegalArgumentException(
					"Checklist with title '" + checkListRequest.getTitle() + "' does not exist in the workspace");
		}

		checklistRepository.delete(checklistOptional.get());
	}

	public List<ChecklistCreateDTO> getMyChecklists(UserEntity user, ChecklistRepository checklistRepository) {
		if (user == null || user.getId() == null) {
			throw new IllegalArgumentException("User is not authenticated or does not have an ID");
		}

		if (user.getWorkspace() == null || user.getWorkspace().getId() == null) {
			throw new IllegalArgumentException("User is not associated with a valid workspace");
		}

		// Fetch all checklists from the user's workspace
		var checklists = checklistRepository.findAllByWorkspaceId(user.getWorkspace().getId());

		if (checklists.isEmpty()) {
			throw new IllegalArgumentException(
					"No checklists found for workspace with ID " + user.getWorkspace().getId());
		}

		// Convert entity to DTO
		return checklists.stream()
				.map(checklist -> ChecklistCreateDTO.builder()
						.title(checklist.getTitle())
						.checklistId(checklist.getId())
						.build())
				.toList();
	}
}

package com.dev.project.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.project.Component.JwtUtil;
import com.dev.project.DTO.ChecklistItemDTO;
import com.dev.project.Entity.ChecklistItemEntity;
import com.dev.project.Repository.ChecklistItemRepository;
import com.dev.project.Repository.ChecklistRepository;
import com.dev.project.Repository.UserRepository;

@Service
public class ChecklistItemService {
	private final ChecklistItemRepository checklistItemRepository;
	private final UserRepository userRepository;

	@Autowired
	public ChecklistItemService(ChecklistItemRepository checklistItemRepository, UserRepository userRepository) {
		this.checklistItemRepository = checklistItemRepository;
		this.userRepository = userRepository;
	}

	// Method to get all checklist items by checklist ID
	public List<ChecklistItemEntity> getChecklistItemsByChecklistId(UUID checklistId) {
		return checklistItemRepository.findAllByChecklistId(checklistId);
	}

	// Method to create a new checklist item
	public ChecklistItemEntity createChecklistItem(String jwtToken, ChecklistItemDTO checklistItemDTO, JwtUtil jwtUtil,
												   UserRepository userRepository, ChecklistRepository checklistRepository) {
		// Extract username from JWT token
		String username = jwtUtil.extractName(jwtToken);

		// Validate token and check if user exists
		var user = userRepository.findByName(username)
				.orElseThrow(() -> new IllegalArgumentException("Invalid token or user does not exist"));

		// Validate checklist ownership
		var checklist = checklistRepository.findById(checklistItemDTO.getChecklistId())
				.orElseThrow(() -> new IllegalArgumentException("Checklist does not exist"));

//		if (!checklist.getCreatedBy().getId().equals(user.getId())) {
//			throw new IllegalArgumentException("User is not authorized to add items to this checklist");
//		}

		var workspace = checklist.getWorkspace();
		if (workspace == null || !workspace.getUsers().contains(user)) {
			throw new IllegalArgumentException("User is not authorized to add items to this checklist");
		}

		// Convert DTO to Entity
		ChecklistItemEntity checklistItemEntity = ChecklistItemEntity.builder()
				.checklist(checklist)
				.content(checklistItemDTO.getContent())
				.isChecked(checklistItemDTO.isChecked())
				.createdAt(new Date())
				.createdBy(user)
				.build();

		// Save the checklist item
		return checklistItemRepository.save(checklistItemEntity);
	}

	// Method to update an existing checklist item
	public ChecklistItemEntity updateChecklistItem(UUID id, ChecklistItemDTO updatedItem, String username) {
		//validate the user
		var user = userRepository.findByName(username)
				.orElseThrow(() -> new IllegalArgumentException("User does not exist"));
		//validate the user owns the checklist item via worksapce
		var checklistItem = checklistItemRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Checklist item does not exist"));
		var checklist = checklistItem.getChecklist();
		var workspace = checklist.getWorkspace();
		if (workspace == null || !workspace.getUsers().contains(user)) {
			throw new IllegalArgumentException("User is not authorized to update this checklist item");
		}
		ChecklistItemEntity existingItem = checklistItemRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Checklist item not found"));
		existingItem.setContent(updatedItem.getContent());
		existingItem.setChecked(updatedItem.isChecked());
		return checklistItemRepository.save(existingItem);
	}

	// Method to delete a checklist item
	public void deleteChecklistItem(UUID id) {
		checklistItemRepository.deleteById(id);
	}
}

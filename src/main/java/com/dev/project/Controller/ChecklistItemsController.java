package com.dev.project.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.project.Component.JwtUtil;
import com.dev.project.DTO.ChecklistItemDTO;
import com.dev.project.Entity.ChecklistItemEntity;
import com.dev.project.Repository.ChecklistRepository;
import com.dev.project.Repository.UserRepository;
import com.dev.project.Service.ChecklistItemService;

@RestController
@RequestMapping("/api/checklist-items")
public class ChecklistItemsController {

	private final ChecklistItemService checklistItemService;
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final ChecklistRepository checklistRepository;

	@Autowired
	public ChecklistItemsController(ChecklistItemService checklistItemService, JwtUtil jwtUtil,
									UserRepository userRepository, ChecklistRepository checklistRepository) {
		this.checklistItemService = checklistItemService;
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
		this.checklistRepository = checklistRepository;
	}

	// Get all checklist items by checklist ID
	@GetMapping("/checklist/{checklistId}")
	public ResponseEntity<List<ChecklistItemEntity>> getChecklistItemsByChecklistId(@PathVariable UUID checklistId) {
		return ResponseEntity.ok(checklistItemService.getChecklistItemsByChecklistId(checklistId));
	}

	@PostMapping("")
	public ResponseEntity<ChecklistItemEntity> createChecklistItem(
			@RequestHeader("Authorization") String authHeader,
			@RequestBody ChecklistItemDTO checklistItem) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new RuntimeException("Missing or invalid Authorization header");
		}

		String token = authHeader.substring(7); // Strip "Bearer "
		return ResponseEntity.ok(checklistItemService.createChecklistItem(token, checklistItem, jwtUtil,
				userRepository, checklistRepository));
	}

	// Update a checklist item
	@PutMapping("/{id}")
	public ResponseEntity<ChecklistItemEntity> updateChecklistItem(@PathVariable UUID id,
																   @RequestHeader("Authorization") String authHeader,
																   @RequestBody ChecklistItemDTO checklistItem) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new RuntimeException("Missing or invalid Authorization header");
		}

		String token = authHeader.substring(7); // Strip "Bearer "
		String username = jwtUtil.extractName(token);
		return ResponseEntity.ok(checklistItemService.updateChecklistItem(id, checklistItem, username));
	}

	// Delete a checklist item
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteChecklistItem(@PathVariable UUID id) {
		checklistItemService.deleteChecklistItem(id);
		return ResponseEntity.noContent().build();
	}
}

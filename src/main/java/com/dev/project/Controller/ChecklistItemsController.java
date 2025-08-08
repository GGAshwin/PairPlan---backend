package com.dev.project.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.dev.project.DTO.MessageDTO;

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
	public ResponseEntity<?> getChecklistItemsByChecklistId(
			@PathVariable UUID checklistId,
			@RequestHeader("Authorization") String authHeader) {

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new RuntimeException("Missing or invalid Authorization header");
		}

		String token = authHeader.substring(7); // Strip "Bearer "

		// Validate the token
		try {
			String username = jwtUtil.extractName(token);
			// You could add additional checks here if needed
			// For example, verify if the user has access to this checklist
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(MessageDTO.error("Invalid or expired token!"));
		}

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
	/// api/checklist-items/checklist/5a67beef-9826-4b9e-bc94-9d9064764c1d
	@PutMapping("/checklist/{checklistId}/{checklistItemId}")
	public ResponseEntity<ChecklistItemEntity> updateChecklistItem(@PathVariable UUID checklistId,
			@PathVariable UUID checklistItemId,
			@RequestHeader("Authorization") String authHeader,
			@RequestBody ChecklistItemDTO checklistItem) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new RuntimeException("Missing or invalid Authorization header");
		}

		String token = authHeader.substring(7); // Strip "Bearer "
		String username = jwtUtil.extractName(token);
		return ResponseEntity
				.ok(checklistItemService.updateChecklistItem(checklistId, checklistItemId, checklistItem, username));
	}

	// Delete a checklist item
	@DeleteMapping("/checklist/{checklistId}/{checkItemId}")
	public ResponseEntity<?> deleteChecklistItem(
			@PathVariable UUID checkItemId,
			@PathVariable UUID checklistId,
			@RequestHeader("Authorization") String authHeader) {

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(MessageDTO.error("Missing or invalid Authorization header"));
		}

		String token = authHeader.substring(7); // Strip "Bearer "

		// Validate the token
		try {
			String username = jwtUtil.extractName(token);
			// You could add additional checks here if needed
			// For example, verify if the user has permission to delete this item
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(MessageDTO.error("Invalid or expired token!"));
		}

		checklistItemService.deleteChecklistItem(checkItemId, checklistId);
		return ResponseEntity.ok().body(MessageDTO.success("Checklist item deleted successfully"));
	}
}

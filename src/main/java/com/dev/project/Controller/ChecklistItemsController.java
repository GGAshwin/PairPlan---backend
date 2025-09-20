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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dev.project.DTO.MessageDTO;

import com.dev.project.Component.JwtUtil;
import com.dev.project.DTO.ChecklistItemDTO;
import com.dev.project.Entity.ChecklistItemEntity;
import com.dev.project.Repository.ChecklistRepository;
import com.dev.project.Repository.UserRepository;
import com.dev.project.Service.ChecklistItemService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/checklist-items")
@Tag(name = "Checklist Items", description = "Checklist item management operations")
@SecurityRequirement(name = "Bearer Authentication")
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
	public ResponseEntity<List<ChecklistItemEntity>> getChecklistItemsByChecklistId(
			@PathVariable UUID checklistId,
			HttpServletRequest request) {

		// Username is now available from the filter
		String username = (String) request.getAttribute("username");

		// You could add additional checks here if needed
		// For example, verify if the user has access to this checklist

		return ResponseEntity.ok(checklistItemService.getChecklistItemsByChecklistId(checklistId));
	}

	@PostMapping("")
	public ResponseEntity<ChecklistItemEntity> createChecklistItem(
			@RequestBody ChecklistItemDTO checklistItem,
			HttpServletRequest request) {
		String username = (String) request.getAttribute("username");
		return ResponseEntity.ok(checklistItemService.createChecklistItem(username, checklistItem, jwtUtil,
				userRepository, checklistRepository));
	}

	// Update a checklist item
	@PutMapping("/checklist/{checklistId}/{checklistItemId}")
	public ResponseEntity<ChecklistItemEntity> updateChecklistItem(@PathVariable UUID checklistId,
			@PathVariable UUID checklistItemId,
			@RequestBody ChecklistItemDTO checklistItem,
			HttpServletRequest request) {
		String username = (String) request.getAttribute("username");
		return ResponseEntity
				.ok(checklistItemService.updateChecklistItem(checklistId, checklistItemId, checklistItem, username));
	}

	// Delete a checklist item
	@DeleteMapping("/checklist/{checklistId}/{checkItemId}")
	public ResponseEntity<MessageDTO> deleteChecklistItem(
			@PathVariable UUID checkItemId,
			@PathVariable UUID checklistId,
			HttpServletRequest request) {

		String username = (String) request.getAttribute("username");
		// You could add additional checks here if needed
		// For example, verify if the user has permission to delete this item

		checklistItemService.deleteChecklistItem(checkItemId, checklistId);
		return ResponseEntity.ok().body(MessageDTO.success("Checklist item deleted successfully"));
	}
}

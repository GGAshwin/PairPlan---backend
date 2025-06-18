package com.dev.project.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.project.Entity.ChecklistItemEntity;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItemEntity, UUID> {
	// This interface will automatically inherit methods for CRUD operations
	// from JpaRepository, such as save, findById, findAll, deleteById, etc.
	// Additional custom query methods can be defined here if needed.
	 List<ChecklistItemEntity> findAllByChecklistId(UUID checklistId);
}

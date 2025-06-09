package com.dev.project.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.project.Entity.WorkspaceEntity;

public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, UUID> {
	// Additional query methods can be defined here if needed
	Optional<WorkspaceEntity> findByJoinCode(String joinCode);
}

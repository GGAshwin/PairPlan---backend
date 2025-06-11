package com.dev.project.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.project.Entity.ChecklistEntity;
import com.dev.project.Entity.WorkspaceEntity;

@Repository
public interface ChecklistRepository extends JpaRepository<ChecklistEntity, UUID> {
	Optional<ChecklistEntity> findByTitle(String title);

	Optional<ChecklistEntity> findByTitleAndWorkspaceId(String title, UUID workspaceId);

	List<ChecklistEntity> findAllByCreatedById(UUID createdById);
}

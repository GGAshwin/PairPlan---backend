package com.dev.project.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dev.project.Entity.ChecklistEntity;
import com.dev.project.Entity.WorkspaceEntity;

@Repository
public interface ChecklistRepository extends JpaRepository<ChecklistEntity, UUID> {
	Optional<ChecklistEntity> findByTitle(String title);

	Optional<ChecklistEntity> findByTitleAndWorkspaceId(String title, UUID workspaceId);

	@Query("SELECT c FROM ChecklistEntity c WHERE c.createdBy.id = :createdById")
	List<ChecklistEntity> findAllByCreatedById(@Param("createdById") UUID createdById);
}

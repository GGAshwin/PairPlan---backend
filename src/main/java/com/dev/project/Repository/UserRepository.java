package com.dev.project.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.project.Entity.UserEntity;
import com.dev.project.Entity.WorkspaceEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {


	// Additional query methods can be defined here if needed
}

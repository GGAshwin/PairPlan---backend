package com.dev.project.DTO;

import java.util.List;
import java.util.UUID;

import com.dev.project.Entity.ChecklistEntity;
import com.dev.project.Entity.UserEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponseDTO {
	private UUID id;
	private String name;
	private List<ChecklistEntity> createdChecklists;
}

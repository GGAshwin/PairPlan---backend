package com.dev.project.DTO;

import java.util.UUID;

import com.dev.project.Entity.ChecklistEntity;
import com.dev.project.Entity.UserEntity;
import com.dev.project.Entity.WorkspaceEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChecklistCreateDTO {
	private String title;
	private UUID workspaceId;
	private UUID createdById;

	public ChecklistEntity toChecklistEntity(WorkspaceEntity workspaceEntity, UserEntity userEntity) {
		return ChecklistEntity.builder()
				.title(this.title)
				.workspace(workspaceEntity)
				.createdBy(userEntity)
				.createdAt(new java.util.Date())
				.build();
	}
}

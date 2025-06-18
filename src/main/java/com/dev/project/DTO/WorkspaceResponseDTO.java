package com.dev.project.DTO;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkspaceResponseDTO {
	private UUID id;
	private String name;
	private String joinCode;
	private Date createdAt;
	private List<UserResponseDTO> users;
}


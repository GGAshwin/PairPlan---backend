package com.dev.project.DTO;

import java.util.Date;
import java.util.UUID;

import com.dev.project.Entity.ChecklistEntity;
import com.dev.project.Entity.UserEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChecklistItemDTO {

	private UUID checklistId;

	private String content;

	private boolean isChecked = false;

}

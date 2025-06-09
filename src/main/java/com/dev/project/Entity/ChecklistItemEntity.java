package com.dev.project.Entity;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "checklist_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChecklistItemEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "checklist_id", nullable = false)
	private ChecklistEntity checklist;

	private String content;

	@Column(name = "is_checked")
	private boolean isChecked = false;

	@ManyToOne
	@JoinColumn(name = "created_by", nullable = false)
	private UserEntity createdBy;

	@Column(name = "created_at")
	private Date createdAt = new Date();
}

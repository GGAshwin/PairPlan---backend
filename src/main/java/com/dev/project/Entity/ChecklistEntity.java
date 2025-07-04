package com.dev.project.Entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "checklists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"createdBy"})
public class ChecklistEntity {

	@Id
	@GeneratedValue
	private UUID id;
	@Column(nullable = false, unique = true)
	private String title;

	@ManyToOne
	@JoinColumn(name = "workspace_id", nullable = false)
	@JsonIgnore
	private WorkspaceEntity workspace;

	@ManyToOne
	@JoinColumn(name = "created_by", nullable = false)
	@JsonIgnore
	private UserEntity createdBy;

	@Column(name = "created_at")
	private Date createdAt = new Date();

	@OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL)
	@Column(nullable = true)
	private List<ChecklistItemEntity> items;

	@Override
	public String toString() {
		return "ChecklistEntity{" +
				"id=" + id +
				", title='" + title + '\'' +
				", workspaceId=" + (workspace != null ? workspace.getId() : null) +
				", createdById=" + (createdBy != null ? createdBy.getId() : null) +
				", createdAt=" + createdAt +
				'}';
	}

}

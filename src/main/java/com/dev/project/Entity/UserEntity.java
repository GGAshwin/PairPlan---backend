package com.dev.project.Entity;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"workspace", "createdChecklists", "createdItems"})
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@Column(unique = true)
	private String name;

	private String password;

	@ManyToOne
	@JoinColumn(name = "workspace_id", nullable = true)
	@JsonIgnore
	private WorkspaceEntity workspace;

	@OneToMany(mappedBy = "createdBy")
	private List<ChecklistEntity> createdChecklists;

	@OneToMany(mappedBy = "createdBy")
	private List<ChecklistItemEntity> createdItems;

	@Override
	public String toString() {
		return "UserEntity{id=" + id + ", name=" + name + "}";
	}

}

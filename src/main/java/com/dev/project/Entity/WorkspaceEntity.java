package com.dev.project.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@ToString(exclude = {"users"})
public class WorkspaceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(unique = false)
	private UUID id;

	private String name;

	@Column(name = "join_code", unique = true, nullable = true)
	private String joinCode;

	@Column(name = "created_at")
	private Date createdAt = new Date();

	@OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
	private List<UserEntity> users = new ArrayList<>();

	@OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
	private List<ChecklistEntity> checklists = new ArrayList<>();

	@Override
	public String toString() {
		return "WorkspaceEntity{id=" + id + ", name=" + name + "}";
	}
}

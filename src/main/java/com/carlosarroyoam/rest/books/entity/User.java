package com.carlosarroyoam.rest.books.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", length = 128, nullable = false)
	private String name;

	@Column(name = "age", nullable = false)
	private Byte age;

	@Column(name = "email", length = 128, nullable = false, unique = true)
	private String email;

	@Column(name = "username", length = 128, nullable = false, unique = true)
	private String username;

	@Column(name = "password", length = 128, nullable = false)
	private String password;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	@Column(name = "role_id", nullable = false)
	private Integer roleId;

	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
	private Role role;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public User(String name, String email, String username, String password, Integer roleId, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
		this.roleId = roleId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}

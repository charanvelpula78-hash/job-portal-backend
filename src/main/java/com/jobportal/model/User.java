package com.jobportal.model;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
=======
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

<<<<<<< HEAD
    @JsonIgnore
    @Column(nullable = false)
    private String passwordHash;
=======
    @Column(nullable = false)
    private String password;
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

<<<<<<< HEAD
    private String phone;

    /** Recruiters: the company they hire for. */
    private String company;

    /** Candidates: comma-separated skills. */
    @Column(length = 1000)
    private String skills;

    @Column(length = 2000)
    private String about;
=======
    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
}

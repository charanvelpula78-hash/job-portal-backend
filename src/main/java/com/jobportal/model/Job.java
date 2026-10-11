package com.jobportal.model;

import jakarta.persistence.*;
<<<<<<< HEAD
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

=======
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
<<<<<<< HEAD
@Getter @Setter @NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
=======
public class Job {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
    private Long id;

    @Column(nullable = false)
    private String title;

<<<<<<< HEAD
    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String location;

    @Column(length = 4000, nullable = false)
    private String description;

    private String salary;

    /** Full-time, Part-time, Internship, Contract ... */
    private String jobType;

    private boolean active = true;

    private LocalDateTime postedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    @PrePersist
    void onCreate() {
        postedAt = LocalDateTime.now();
    }
=======
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private String location;
    private String jobType;      // Full-time, Part-time, Internship, Contract, Remote
    private String salary;
    private String skills;       // comma separated
    private String experience;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by")
    private User postedBy;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public User getPostedBy() { return postedBy; }
    public void setPostedBy(User postedBy) { this.postedBy = postedBy; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
}

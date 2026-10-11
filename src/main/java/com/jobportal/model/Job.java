package com.jobportal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Getter @Setter @NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

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
}

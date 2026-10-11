package com.jobportal.controller;

<<<<<<< HEAD
import com.jobportal.model.Job;
import com.jobportal.model.Role;
import com.jobportal.model.User;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
=======
import com.jobportal.dto.Dtos.*;
import com.jobportal.service.AdminService;
import org.springframework.web.bind.annotation.*;
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

<<<<<<< HEAD
    private final UserRepository users;
    private final JobRepository jobs;
    private final ApplicationRepository applications;

    public AdminController(UserRepository users, JobRepository jobs, ApplicationRepository applications) {
        this.users = users;
        this.jobs = jobs;
        this.applications = applications;
    }

    @GetMapping("/stats")
    public Map<String, Long> stats() {
        List<User> all = users.findAll();
        long candidates = all.stream().filter(u -> u.getRole() == Role.CANDIDATE).count();
        long recruiters = all.stream().filter(u -> u.getRole() == Role.RECRUITER).count();
        return Map.of("candidates", candidates,
                      "recruiters", recruiters,
                      "jobs", jobs.count(),
                      "applications", applications.count());
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        return users.findAll();
    }

    @GetMapping("/jobs")
    public List<Job> allJobs() {
        return jobs.findAll();
    }

    @DeleteMapping("/users/{id}")
    @Transactional
    public void deleteUser(@PathVariable Long id) {
        User u = users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (u.getRole() == Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Administrator accounts cannot be deleted");
        }
        applications.deleteByCandidateId(id);
        for (Job j : jobs.findByRecruiterIdOrderByPostedAtDesc(id)) {
            applications.deleteByJobId(j.getId());
            jobs.delete(j);
        }
        users.delete(u);
    }

    @DeleteMapping("/jobs/{id}")
    @Transactional
    public void deleteJob(@PathVariable Long id) {
        applications.deleteByJobId(id);
        jobs.deleteById(id);
=======
    private final AdminService admin;

    public AdminController(AdminService admin) {
        this.admin = admin;
    }

    @GetMapping("/stats")
    public StatsDto stats() {
        return admin.stats();
    }

    @GetMapping("/users")
    public List<UserDto> users() {
        return admin.users();
    }

    @PutMapping("/users/{id}/active")
    public UserDto setActive(@PathVariable Long id, @RequestParam boolean value) {
        return admin.setActive(id, value);
    }

    @DeleteMapping("/users/{id}")
    public Map<String, String> deleteUser(@PathVariable Long id) {
        admin.deleteUser(id);
        return Map.of("message", "User deleted");
    }

    @GetMapping("/jobs")
    public List<JobDto> jobs() {
        return admin.jobs();
    }

    @PutMapping("/jobs/{id}/active")
    public JobDto setJobActive(@PathVariable Long id, @RequestParam boolean value) {
        return admin.setJobActive(id, value);
    }

    @DeleteMapping("/jobs/{id}")
    public Map<String, String> deleteJob(@PathVariable Long id) {
        admin.deleteJob(id);
        return Map.of("message", "Job deleted");
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
    }
}

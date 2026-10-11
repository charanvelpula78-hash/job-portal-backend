package com.jobportal.controller;

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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

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
    }
}

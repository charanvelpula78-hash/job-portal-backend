package com.jobportal.controller;

import com.jobportal.dto.JobRequest;
import com.jobportal.model.Job;
import com.jobportal.model.Role;
import com.jobportal.model.User;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobRepository jobs;
    private final UserRepository users;
    private final ApplicationRepository applications;

    public JobController(JobRepository jobs, UserRepository users, ApplicationRepository applications) {
        this.jobs = jobs;
        this.users = users;
        this.applications = applications;
    }

    @GetMapping
    public List<Job> search(@RequestParam(defaultValue = "") String keyword,
                            @RequestParam(defaultValue = "") String location) {
        return jobs.search(keyword.trim(), location.trim());
    }

    @GetMapping("/{id}")
    public Job get(@PathVariable Long id) {
        return jobs.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
    }

    @GetMapping("/recruiter/{recruiterId}")
    public List<Job> byRecruiter(@PathVariable Long recruiterId) {
        return jobs.findByRecruiterIdOrderByPostedAtDesc(recruiterId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Job create(@Valid @RequestBody JobRequest req) {
        User recruiter = users.findById(req.recruiterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recruiter not found"));
        if (recruiter.getRole() != Role.RECRUITER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only recruiters can post jobs");
        }
        Job j = new Job();
        copy(j, req);
        j.setRecruiter(recruiter);
        return jobs.save(j);
    }

    @PutMapping("/{id}")
    public Job update(@PathVariable Long id, @Valid @RequestBody JobRequest req) {
        Job j = get(id);
        if (!j.getRecruiter().getId().equals(req.recruiterId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only edit your own jobs");
        }
        copy(j, req);
        return jobs.save(j);
    }

    /** Opens or closes a vacancy without deleting it. */
    @PutMapping("/{id}/active")
    public Job setActive(@PathVariable Long id, @RequestParam boolean value) {
        Job j = get(id);
        j.setActive(value);
        return jobs.save(j);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable Long id) {
        applications.deleteByJobId(id);
        jobs.deleteById(id);
    }

    private void copy(Job j, JobRequest req) {
        j.setTitle(req.title());
        j.setCompany(req.company());
        j.setLocation(req.location());
        j.setDescription(req.description());
        j.setSalary(req.salary());
        j.setJobType(req.jobType());
    }
}

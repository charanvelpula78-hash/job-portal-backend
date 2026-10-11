package com.jobportal.controller;

<<<<<<< HEAD
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
=======
import com.jobportal.dto.Dtos.*;
import com.jobportal.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e

@RestController
@RequestMapping("/api/jobs")
public class JobController {

<<<<<<< HEAD
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
=======
    private final JobService jobs;

    public JobController(JobService jobs) {
        this.jobs = jobs;
    }

    @GetMapping
    public PageResponse<JobDto> search(@RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) String location,
                                       @RequestParam(required = false) String type,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return jobs.search(keyword, location, type, page, size);
    }

    @GetMapping("/mine")
    public List<JobDto> mine() {
        return jobs.mine();
    }

    @GetMapping("/{id}")
    public JobDto get(@PathVariable Long id) {
        return jobs.get(id);
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
<<<<<<< HEAD
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
=======
    public JobDto create(@Valid @RequestBody JobRequest req) {
        return jobs.create(req);
    }

    @PutMapping("/{id}")
    public JobDto update(@PathVariable Long id, @Valid @RequestBody JobRequest req) {
        return jobs.update(id, req);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        jobs.delete(id);
        return Map.of("message", "Job deleted");
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
    }
}

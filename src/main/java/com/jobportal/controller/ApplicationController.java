package com.jobportal.controller;

<<<<<<< HEAD
import com.jobportal.dto.ApplyRequest;
import com.jobportal.dto.StatusRequest;
import com.jobportal.model.*;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
=======
import com.jobportal.dto.Dtos.*;
import com.jobportal.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

<<<<<<< HEAD
    private final ApplicationRepository applications;
    private final JobRepository jobs;
    private final UserRepository users;

    public ApplicationController(ApplicationRepository applications, JobRepository jobs, UserRepository users) {
        this.applications = applications;
        this.jobs = jobs;
        this.users = users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplication apply(@Valid @RequestBody ApplyRequest req) {
        Job job = jobs.findById(req.jobId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        User candidate = users.findById(req.candidateId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));
        if (candidate.getRole() != Role.CANDIDATE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only candidates can apply for jobs");
        }
        if (!job.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This job is no longer accepting applications");
        }
        if (applications.existsByJobIdAndCandidateId(job.getId(), candidate.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already applied for this job");
        }
        JobApplication a = new JobApplication();
        a.setJob(job);
        a.setCandidate(candidate);
        a.setCoverNote(req.coverNote());
        return applications.save(a);
    }

    @GetMapping("/candidate/{candidateId}")
    public List<JobApplication> forCandidate(@PathVariable Long candidateId) {
        return applications.findByCandidateIdOrderByAppliedAtDesc(candidateId);
    }

    @GetMapping("/job/{jobId}")
    public List<JobApplication> forJob(@PathVariable Long jobId) {
        return applications.findByJobIdOrderByAppliedAtDesc(jobId);
    }

    @PutMapping("/{id}/status")
    public JobApplication updateStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest req) {
        JobApplication a = applications.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
        a.setStatus(req.status());
        return applications.save(a);
=======
    private final ApplicationService applications;

    public ApplicationController(ApplicationService applications) {
        this.applications = applications;
    }

    @PostMapping("/jobs/{jobId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationDto apply(@PathVariable Long jobId, @RequestBody(required = false) ApplyRequest req) {
        return applications.apply(jobId, req);
    }

    @GetMapping("/mine")
    public List<ApplicationDto> mine() {
        return applications.mine();
    }

    @GetMapping("/job/{jobId}")
    public List<ApplicationDto> forJob(@PathVariable Long jobId) {
        return applications.forJob(jobId);
    }

    @PutMapping("/{id}/status")
    public ApplicationDto updateStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest req) {
        return applications.updateStatus(id, req.status());
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
    }
}

package com.jobportal.controller;

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

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

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
    }
}

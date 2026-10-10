package com.jobportal.controller;

import com.jobportal.dto.Dtos.*;
import com.jobportal.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

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
    }
}

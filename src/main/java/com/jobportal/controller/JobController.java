package com.jobportal.controller;

import com.jobportal.dto.Dtos.*;
import com.jobportal.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

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
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
    }
}

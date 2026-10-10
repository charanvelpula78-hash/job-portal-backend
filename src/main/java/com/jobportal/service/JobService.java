package com.jobportal.service;

import com.jobportal.dto.Dtos.*;
import com.jobportal.exception.ApiException;
import com.jobportal.model.Company;
import com.jobportal.model.Job;
import com.jobportal.model.Role;
import com.jobportal.model.User;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.CompanyRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.security.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobService {

    private final JobRepository jobs;
    private final CompanyRepository companies;
    private final ApplicationRepository applications;
    private final CurrentUser currentUser;

    public JobService(JobRepository jobs, CompanyRepository companies,
                      ApplicationRepository applications, CurrentUser currentUser) {
        this.jobs = jobs;
        this.companies = companies;
        this.applications = applications;
        this.currentUser = currentUser;
    }

    public JobDto toDto(Job j) {
        Long recruiterId = j.getPostedBy().getId();
        String company = companies.findByUserId(recruiterId)
                .map(Company::getCompanyName).orElse(j.getPostedBy().getName());
        return new JobDto(j.getId(), j.getTitle(), j.getDescription(), j.getLocation(), j.getJobType(),
                j.getSalary(), j.getSkills(), j.getExperience(), j.isActive(), j.getCreatedAt(),
                recruiterId, company, applications.countByJobId(j.getId()));
    }

    private static String clean(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    @Transactional(readOnly = true)
    public PageResponse<JobDto> search(String keyword, String location, String type, int page, int size) {
        Page<Job> result = jobs.search(clean(keyword), clean(location), clean(type),
                PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50)));
        List<JobDto> content = result.getContent().stream().map(this::toDto).toList();
        return new PageResponse<>(content, result.getNumber(), result.getTotalPages(), result.getTotalElements());
    }

    @Transactional(readOnly = true)
    public JobDto get(Long id) {
        Job job = jobs.findById(id).orElseThrow(() -> ApiException.notFound("Job"));
        if (!job.isActive()) {
            // hidden jobs are only visible to their owner and to admins
            User viewer = currentUser.getOrNull();
            boolean allowed = viewer != null && (viewer.getRole() == Role.ADMIN
                    || viewer.getId().equals(job.getPostedBy().getId()));
            if (!allowed) throw ApiException.notFound("Job");
        }
        return toDto(job);
    }

    @Transactional(readOnly = true)
    public List<JobDto> mine() {
        User me = currentUser.get();
        return jobs.findByPostedByIdOrderByCreatedAtDesc(me.getId()).stream().map(this::toDto).toList();
    }

    public JobDto create(JobRequest req) {
        Job job = new Job();
        job.setPostedBy(currentUser.get());
        apply(job, req);
        return toDto(jobs.save(job));
    }

    public JobDto update(Long id, JobRequest req) {
        Job job = owned(id);
        apply(job, req);
        return toDto(jobs.save(job));
    }

    public void delete(Long id) {
        Job job = owned(id);
        applications.deleteByJobId(job.getId());
        jobs.delete(job);
    }

    private Job owned(Long id) {
        Job job = jobs.findById(id).orElseThrow(() -> ApiException.notFound("Job"));
        if (!job.getPostedBy().getId().equals(currentUser.get().getId())) {
            throw ApiException.forbidden("You can only change your own job postings");
        }
        return job;
    }

    private void apply(Job job, JobRequest r) {
        job.setTitle(r.title().trim());
        job.setDescription(r.description().trim());
        job.setLocation(clean(r.location()));
        job.setJobType(clean(r.jobType()));
        job.setSalary(clean(r.salary()));
        job.setSkills(clean(r.skills()));
        job.setExperience(clean(r.experience()));
    }
}

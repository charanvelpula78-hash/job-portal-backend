package com.jobportal.service;

import com.jobportal.dto.Dtos.*;
import com.jobportal.exception.ApiException;
import com.jobportal.model.*;
import com.jobportal.repository.*;
import com.jobportal.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminService {

    private final UserRepository users;
    private final JobRepository jobs;
    private final ApplicationRepository applications;
    private final CandidateProfileRepository profiles;
    private final CompanyRepository companies;
    private final JobService jobService;
    private final CurrentUser currentUser;

    public AdminService(UserRepository users, JobRepository jobs, ApplicationRepository applications,
                        CandidateProfileRepository profiles, CompanyRepository companies,
                        JobService jobService, CurrentUser currentUser) {
        this.users = users;
        this.jobs = jobs;
        this.applications = applications;
        this.profiles = profiles;
        this.companies = companies;
        this.jobService = jobService;
        this.currentUser = currentUser;
    }

    @Transactional(readOnly = true)
    public StatsDto stats() {
        return new StatsDto(users.countByRole(Role.CANDIDATE), users.countByRole(Role.RECRUITER),
                jobs.count(), jobs.countByActiveTrue(), applications.count());
    }

    @Transactional(readOnly = true)
    public List<UserDto> users() {
        return users.findAll().stream().map(AuthService::toDto).toList();
    }

    public UserDto setActive(Long userId, boolean active) {
        User u = users.findById(userId).orElseThrow(() -> ApiException.notFound("User"));
        if (u.getId().equals(currentUser.get().getId())) {
            throw ApiException.badRequest("You cannot block your own account");
        }
        u.setActive(active);
        return AuthService.toDto(users.save(u));
    }

    public void deleteUser(Long userId) {
        User u = users.findById(userId).orElseThrow(() -> ApiException.notFound("User"));
        if (u.getRole() == Role.ADMIN) throw ApiException.badRequest("Administrator accounts cannot be deleted");
        // remove everything that references this user, then the user
        applications.deleteByCandidateId(u.getId());
        for (Job j : jobs.findByPostedByIdOrderByCreatedAtDesc(u.getId())) {
            applications.deleteByJobId(j.getId());
            jobs.delete(j);
        }
        profiles.deleteByUserId(u.getId());
        companies.deleteByUserId(u.getId());
        users.delete(u);
    }

    @Transactional(readOnly = true)
    public List<JobDto> jobs() {
        return jobs.findAllByOrderByCreatedAtDesc().stream().map(jobService::toDto).toList();
    }

    public JobDto setJobActive(Long jobId, boolean active) {
        Job j = jobs.findById(jobId).orElseThrow(() -> ApiException.notFound("Job"));
        j.setActive(active);
        return jobService.toDto(jobs.save(j));
    }

    public void deleteJob(Long jobId) {
        Job j = jobs.findById(jobId).orElseThrow(() -> ApiException.notFound("Job"));
        applications.deleteByJobId(jobId);
        jobs.delete(j);
    }
}

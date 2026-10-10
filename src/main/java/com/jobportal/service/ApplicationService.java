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
public class ApplicationService {

    private final ApplicationRepository applications;
    private final JobRepository jobs;
    private final CandidateProfileRepository profiles;
    private final CompanyRepository companies;
    private final CurrentUser currentUser;

    public ApplicationService(ApplicationRepository applications, JobRepository jobs,
                              CandidateProfileRepository profiles, CompanyRepository companies,
                              CurrentUser currentUser) {
        this.applications = applications;
        this.jobs = jobs;
        this.profiles = profiles;
        this.companies = companies;
        this.currentUser = currentUser;
    }

    private ApplicationDto toDto(Application a) {
        User c = a.getCandidate();
        Job j = a.getJob();
        CandidateProfile p = profiles.findByUserId(c.getId()).orElse(null);
        String company = companies.findByUserId(j.getPostedBy().getId())
                .map(Company::getCompanyName).orElse(j.getPostedBy().getName());
        return new ApplicationDto(a.getId(), j.getId(), j.getTitle(), company,
                c.getId(), c.getName(), c.getEmail(), a.getStatus(), a.getCoverLetter(), a.getAppliedAt(),
                p == null ? null : p.getHeadline(), p == null ? null : p.getSkills(),
                p == null ? null : p.getEducation(), p == null ? null : p.getExperience(),
                p == null ? null : p.getPhone(),
                p != null && p.getResumeFile() != null);
    }

    public ApplicationDto apply(Long jobId, ApplyRequest req) {
        User me = currentUser.get();
        Job job = jobs.findById(jobId).orElseThrow(() -> ApiException.notFound("Job"));
        if (!job.isActive()) throw ApiException.badRequest("This job is no longer accepting applications");
        if (applications.existsByJobIdAndCandidateId(jobId, me.getId())) {
            throw ApiException.conflict("You have already applied for this job");
        }
        Application a = new Application();
        a.setJob(job);
        a.setCandidate(me);
        a.setCoverLetter(req == null || req.coverLetter() == null || req.coverLetter().isBlank()
                ? null : req.coverLetter().trim());
        return toDto(applications.save(a));
    }

    @Transactional(readOnly = true)
    public List<ApplicationDto> mine() {
        return applications.findByCandidateIdOrderByAppliedAtDesc(currentUser.get().getId())
                .stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationDto> forJob(Long jobId) {
        Job job = jobs.findById(jobId).orElseThrow(() -> ApiException.notFound("Job"));
        if (!job.getPostedBy().getId().equals(currentUser.get().getId())) {
            throw ApiException.forbidden("You can only view applicants for your own jobs");
        }
        return applications.findByJobIdOrderByAppliedAtDesc(jobId).stream().map(this::toDto).toList();
    }

    public ApplicationDto updateStatus(Long applicationId, ApplicationStatus status) {
        Application a = applications.findById(applicationId)
                .orElseThrow(() -> ApiException.notFound("Application"));
        if (!a.getJob().getPostedBy().getId().equals(currentUser.get().getId())) {
            throw ApiException.forbidden("You can only update applications for your own jobs");
        }
        a.setStatus(status);
        return toDto(applications.save(a));
    }
}

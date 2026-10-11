package com.jobportal.service;

import com.jobportal.dto.Dtos.*;
import com.jobportal.exception.ApiException;
import com.jobportal.model.*;
import com.jobportal.repository.*;
import com.jobportal.security.CurrentUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

@Service
@Transactional
public class ProfileService {

    private final CandidateProfileRepository profiles;
    private final CompanyRepository companies;
    private final ApplicationRepository applications;
    private final CurrentUser currentUser;
    private final Path uploadDir;

    public ProfileService(CandidateProfileRepository profiles, CompanyRepository companies,
                          ApplicationRepository applications, CurrentUser currentUser,
                          @Value("${app.upload-dir}") String uploadDir) throws IOException {
        this.profiles = profiles;
        this.companies = companies;
        this.applications = applications;
        this.currentUser = currentUser;
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }

    // ---------- candidate ----------
    private CandidateProfile profileOf(User u) {
        return profiles.findByUserId(u.getId()).orElseGet(() -> {
            CandidateProfile p = new CandidateProfile();
            p.setUser(u);
            return profiles.save(p);
        });
    }

    private CandidateProfileDto toDto(CandidateProfile p) {
        return new CandidateProfileDto(p.getHeadline(), p.getPhone(), p.getLocation(), p.getSkills(),
                p.getEducation(), p.getExperience(), p.getResumeOriginal());
    }

    public CandidateProfileDto getCandidate() {
        return toDto(profileOf(currentUser.get()));
    }

    public CandidateProfileDto updateCandidate(CandidateProfileDto d) {
        CandidateProfile p = profileOf(currentUser.get());
        p.setHeadline(d.headline());
        p.setPhone(d.phone());
        p.setLocation(d.location());
        p.setSkills(d.skills());
        p.setEducation(d.education());
        p.setExperience(d.experience());
        return toDto(profiles.save(p));
    }

    public CandidateProfileDto uploadResume(MultipartFile file) {
        if (file == null || file.isEmpty()) throw ApiException.badRequest("Choose a file to upload");
        String original = Paths.get(file.getOriginalFilename() == null ? "resume" : file.getOriginalFilename())
                .getFileName().toString();
        String lower = original.toLowerCase(Locale.ROOT);
        if (!(lower.endsWith(".pdf") || lower.endsWith(".doc") || lower.endsWith(".docx"))) {
            throw ApiException.badRequest("Resume must be a PDF, DOC or DOCX file");
        }
        User me = currentUser.get();
        CandidateProfile p = profileOf(me);
        String ext = lower.substring(lower.lastIndexOf('.'));
        String stored = "resume_" + me.getId() + "_" + System.currentTimeMillis() + ext;
        try {
            Files.copy(file.getInputStream(), uploadDir.resolve(stored), StandardCopyOption.REPLACE_EXISTING);
            if (p.getResumeFile() != null) Files.deleteIfExists(uploadDir.resolve(p.getResumeFile()));
        } catch (IOException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save the file");
        }
        p.setResumeFile(stored);
        p.setResumeOriginal(original);
        return toDto(profiles.save(p));
    }

    /** Candidate themselves, admin, or a recruiter who received an application from this candidate. */
    public CandidateProfile resumeFor(Long candidateUserId) {
        User me = currentUser.get();
        boolean allowed = me.getId().equals(candidateUserId)
                || me.getRole() == Role.ADMIN
                || (me.getRole() == Role.RECRUITER
                    && applications.existsByCandidateIdAndJobPostedById(candidateUserId, me.getId()));
        if (!allowed) throw ApiException.forbidden("You cannot view this resume");
        CandidateProfile p = profiles.findByUserId(candidateUserId)
                .orElseThrow(() -> ApiException.notFound("Resume"));
        if (p.getResumeFile() == null) throw ApiException.notFound("Resume");
        return p;
    }

    public Resource loadResume(CandidateProfile p) {
        try {
            Resource r = new UrlResource(uploadDir.resolve(p.getResumeFile()).toUri());
            if (!r.exists()) throw ApiException.notFound("Resume file");
            return r;
        } catch (java.net.MalformedURLException e) {
            throw ApiException.notFound("Resume file");
        }
    }

    // ---------- recruiter / company ----------
    private Company companyOf(User u) {
        return companies.findByUserId(u.getId()).orElseGet(() -> {
            Company c = new Company();
            c.setUser(u);
            c.setCompanyName(u.getName());
            return companies.save(c);
        });
    }

    private CompanyDto toDto(Company c) {
        return new CompanyDto(c.getCompanyName(), c.getWebsite(), c.getLocation(), c.getDescription());
    }

    public CompanyDto getCompany() {
        return toDto(companyOf(currentUser.get()));
    }

    public CompanyDto updateCompany(CompanyDto d) {
        if (d.companyName() == null || d.companyName().isBlank()) {
            throw ApiException.badRequest("Company name is required");
        }
        Company c = companyOf(currentUser.get());
        c.setCompanyName(d.companyName().trim());
        c.setWebsite(d.website());
        c.setLocation(d.location());
        c.setDescription(d.description());
        return toDto(companies.save(c));
    }
}

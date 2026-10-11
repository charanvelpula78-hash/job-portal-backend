package com.jobportal.controller;

import com.jobportal.dto.Dtos.*;
import com.jobportal.model.CandidateProfile;
import com.jobportal.service.ProfileService;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profiles;

    public ProfileController(ProfileService profiles) {
        this.profiles = profiles;
    }

    @GetMapping("/candidate")
    public CandidateProfileDto getCandidate() {
        return profiles.getCandidate();
    }

    @PutMapping("/candidate")
    public CandidateProfileDto updateCandidate(@RequestBody CandidateProfileDto dto) {
        return profiles.updateCandidate(dto);
    }

    @PostMapping(value = "/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CandidateProfileDto uploadResume(@RequestParam("file") MultipartFile file) {
        return profiles.uploadResume(file);
    }

    @GetMapping("/resume/{userId}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long userId) {
        CandidateProfile p = profiles.resumeFor(userId);
        Resource file = profiles.loadResume(p);
        String name = p.getResumeOriginal() == null ? "resume" : p.getResumeOriginal();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(name).build().toString())
                .body(file);
    }

    @GetMapping("/company")
    public CompanyDto getCompany() {
        return profiles.getCompany();
    }

    @PutMapping("/company")
    public CompanyDto updateCompany(@RequestBody CompanyDto dto) {
        return profiles.updateCompany(dto);
    }
}

package com.jobportal.dto;

import com.jobportal.model.ApplicationStatus;
import com.jobportal.model.Role;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public final class Dtos {
    private Dtos() {}

    public record RegisterRequest(
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotBlank @Size(min = 6, message = "Password must be at least 6 characters") String password,
            @NotNull Role role,
            String companyName) {}

    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}

    public record UserDto(Long id, String name, String email, Role role, boolean active, LocalDateTime createdAt) {}

    public record AuthResponse(String token, UserDto user) {}

    public record JobRequest(
            @NotBlank String title,
            @NotBlank String description,
            String location, String jobType, String salary, String skills, String experience) {}

    public record JobDto(Long id, String title, String description, String location, String jobType,
                         String salary, String skills, String experience, boolean active,
                         LocalDateTime createdAt, Long recruiterId, String companyName,
                         long applicantCount) {}

    public record ApplyRequest(String coverLetter) {}

    public record StatusRequest(@NotNull ApplicationStatus status) {}

    public record ApplicationDto(Long id, Long jobId, String jobTitle, String companyName,
                                 Long candidateId, String candidateName, String candidateEmail,
                                 ApplicationStatus status, String coverLetter, LocalDateTime appliedAt,
                                 String headline, String skills, String education, String experience,
                                 String phone, boolean hasResume) {}

    public record CandidateProfileDto(String headline, String phone, String location, String skills,
                                      String education, String experience, String resumeName) {}

    public record CompanyDto(String companyName, String website, String location, String description) {}

    public record PageResponse<T>(List<T> content, int page, int totalPages, long totalElements) {}

    public record StatsDto(long candidates, long recruiters, long jobs, long activeJobs, long applications) {}
}

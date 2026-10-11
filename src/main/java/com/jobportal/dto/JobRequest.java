package com.jobportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JobRequest(
        @NotBlank String title,
        @NotBlank String company,
        @NotBlank String location,
        @NotBlank String description,
        String salary,
        String jobType,
        @NotNull Long recruiterId) {}

package com.jobportal.dto;

import jakarta.validation.constraints.NotNull;

public record ApplyRequest(@NotNull Long jobId, @NotNull Long candidateId, String coverNote) {}

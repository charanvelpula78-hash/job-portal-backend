package com.jobportal.repository;

import com.jobportal.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByCandidateIdOrderByAppliedAtDesc(Long candidateId);
    List<JobApplication> findByJobIdOrderByAppliedAtDesc(Long jobId);
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
    void deleteByJobId(Long jobId);
    void deleteByCandidateId(Long candidateId);
}

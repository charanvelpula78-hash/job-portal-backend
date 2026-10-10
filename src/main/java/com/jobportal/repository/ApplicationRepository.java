package com.jobportal.repository;

import com.jobportal.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
    List<Application> findByCandidateIdOrderByAppliedAtDesc(Long candidateId);
    List<Application> findByJobIdOrderByAppliedAtDesc(Long jobId);
    long countByJobId(Long jobId);
    boolean existsByCandidateIdAndJobPostedById(Long candidateId, Long recruiterId);
    void deleteByJobId(Long jobId);
    void deleteByCandidateId(Long candidateId);
}

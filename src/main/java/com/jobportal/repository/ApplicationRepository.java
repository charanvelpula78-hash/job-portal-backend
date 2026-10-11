package com.jobportal.repository;

<<<<<<< HEAD
import com.jobportal.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByCandidateIdOrderByAppliedAtDesc(Long candidateId);
    List<JobApplication> findByJobIdOrderByAppliedAtDesc(Long jobId);
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
=======
import com.jobportal.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
    List<Application> findByCandidateIdOrderByAppliedAtDesc(Long candidateId);
    List<Application> findByJobIdOrderByAppliedAtDesc(Long jobId);
    long countByJobId(Long jobId);
    boolean existsByCandidateIdAndJobPostedById(Long candidateId, Long recruiterId);
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
    void deleteByJobId(Long jobId);
    void deleteByCandidateId(Long candidateId);
}

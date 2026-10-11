package com.jobportal.repository;

import com.jobportal.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByRecruiterIdOrderByPostedAtDesc(Long recruiterId);

    @Query("""
           select j from Job j
           where j.active = true
             and (:keyword = '' or lower(j.title) like lower(concat('%', :keyword, '%'))
                               or lower(j.company) like lower(concat('%', :keyword, '%')))
             and (:location = '' or lower(j.location) like lower(concat('%', :location, '%')))
           order by j.postedAt desc
           """)
    List<Job> search(@Param("keyword") String keyword, @Param("location") String location);
}

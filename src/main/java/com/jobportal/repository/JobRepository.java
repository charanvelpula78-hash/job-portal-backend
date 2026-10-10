package com.jobportal.repository;

import com.jobportal.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("""
           select j from Job j
           where j.active = true
             and (:kw is null or lower(j.title) like lower(concat('%', :kw, '%'))
                              or lower(j.skills) like lower(concat('%', :kw, '%')))
             and (:loc is null or lower(j.location) like lower(concat('%', :loc, '%')))
             and (:type is null or j.jobType = :type)
           order by j.createdAt desc
           """)
    Page<Job> search(@Param("kw") String keyword,
                     @Param("loc") String location,
                     @Param("type") String type,
                     Pageable pageable);

    List<Job> findByPostedByIdOrderByCreatedAtDesc(Long recruiterId);
    List<Job> findAllByOrderByCreatedAtDesc();
    long countByActiveTrue();
}

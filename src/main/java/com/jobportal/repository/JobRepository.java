package com.jobportal.repository;

import com.jobportal.model.Job;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

=======
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

<<<<<<< HEAD
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
=======
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
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
}

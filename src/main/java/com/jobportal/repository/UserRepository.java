package com.jobportal.repository;

<<<<<<< HEAD
import com.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

=======
import com.jobportal.model.Role;
import com.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
<<<<<<< HEAD
=======
    long countByRole(Role role);
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
}

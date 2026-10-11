package com.jobportal;

import com.jobportal.model.Role;
import com.jobportal.model.User;
import com.jobportal.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class JobPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobPortalApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Creates a default administrator the first time the app starts. */
    @Bean
    CommandLineRunner seedAdmin(UserRepository users, PasswordEncoder encoder) {
        return args -> {
            if (!users.existsByEmail("admin@jobportal.com")) {
                User admin = new User();
                admin.setName("Administrator");
                admin.setEmail("admin@jobportal.com");
                admin.setPasswordHash(encoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                users.save(admin);
                System.out.println("Default admin created: admin@jobportal.com / admin123");
            }
        };
    }
}

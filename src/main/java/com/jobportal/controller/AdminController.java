package com.jobportal.controller;

import com.jobportal.dto.Dtos.*;
import com.jobportal.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService admin;

    public AdminController(AdminService admin) {
        this.admin = admin;
    }

    @GetMapping("/stats")
    public StatsDto stats() {
        return admin.stats();
    }

    @GetMapping("/users")
    public List<UserDto> users() {
        return admin.users();
    }

    @PutMapping("/users/{id}/active")
    public UserDto setActive(@PathVariable Long id, @RequestParam boolean value) {
        return admin.setActive(id, value);
    }

    @DeleteMapping("/users/{id}")
    public Map<String, String> deleteUser(@PathVariable Long id) {
        admin.deleteUser(id);
        return Map.of("message", "User deleted");
    }

    @GetMapping("/jobs")
    public List<JobDto> jobs() {
        return admin.jobs();
    }

    @PutMapping("/jobs/{id}/active")
    public JobDto setJobActive(@PathVariable Long id, @RequestParam boolean value) {
        return admin.setJobActive(id, value);
    }

    @DeleteMapping("/jobs/{id}")
    public Map<String, String> deleteJob(@PathVariable Long id) {
        admin.deleteJob(id);
        return Map.of("message", "Job deleted");
    }
}

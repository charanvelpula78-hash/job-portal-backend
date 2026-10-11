package com.jobportal.controller;

<<<<<<< HEAD
import com.jobportal.dto.LoginRequest;
import com.jobportal.dto.RegisterRequest;
import com.jobportal.model.Role;
import com.jobportal.model.User;
import com.jobportal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
=======
import com.jobportal.dto.Dtos.*;
import com.jobportal.security.CurrentUser;
import com.jobportal.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e

@RestController
@RequestMapping("/api/auth")
public class AuthController {

<<<<<<< HEAD
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
=======
    private final AuthService auth;
    private final CurrentUser currentUser;

    public AuthController(AuthService auth, CurrentUser currentUser) {
        this.auth = auth;
        this.currentUser = currentUser;
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
<<<<<<< HEAD
    public User register(@Valid @RequestBody RegisterRequest req) {
        if (req.role() == Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin accounts cannot be self-registered");
        }
        if (users.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        }
        User u = new User();
        u.setName(req.name());
        u.setEmail(req.email());
        u.setPasswordHash(encoder.encode(req.password()));
        u.setRole(req.role());
        u.setPhone(req.phone());
        u.setCompany(req.company());
        u.setSkills(req.skills());
        u.setAbout(req.about());
        return users.save(u);
    }

    @PostMapping("/login")
    public User login(@Valid @RequestBody LoginRequest req) {
        User u = users.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect email or password"));
        if (!encoder.matches(req.password(), u.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect email or password");
        }
        return u;
    }

    @GetMapping("/users/{id}")
    public User profile(@PathVariable Long id) {
        return users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PutMapping("/users/{id}")
    public User updateProfile(@PathVariable Long id, @RequestBody User changes) {
        User u = profile(id);
        u.setName(changes.getName() != null ? changes.getName() : u.getName());
        u.setPhone(changes.getPhone());
        u.setCompany(changes.getCompany());
        u.setSkills(changes.getSkills());
        u.setAbout(changes.getAbout());
        return users.save(u);
=======
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        return auth.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return auth.login(req);
    }

    /** Lets the frontend re-check who is logged in after a page refresh. */
    @GetMapping("/me")
    public UserDto me() {
        return AuthService.toDto(currentUser.get());
>>>>>>> 68b12bc93b1bba881f51df708fd58b4797bbdf1e
    }
}

package com.jobportal.service;

import com.jobportal.dto.Dtos.*;
import com.jobportal.exception.ApiException;
import com.jobportal.model.*;
import com.jobportal.repository.*;
import com.jobportal.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository users;
    private final CandidateProfileRepository candidates;
    private final CompanyRepository companies;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository users, CandidateProfileRepository candidates,
                       CompanyRepository companies, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.candidates = candidates;
        this.companies = companies;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public static UserDto toDto(User u) {
        return new UserDto(u.getId(), u.getName(), u.getEmail(), u.getRole(), u.isActive(), u.getCreatedAt());
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (req.role() == Role.ADMIN) {
            throw ApiException.badRequest("Choose either Candidate or Recruiter");
        }
        String email = req.email().trim().toLowerCase();
        if (users.existsByEmail(email)) {
            throw ApiException.conflict("An account with this email already exists");
        }

        User user = new User();
        user.setName(req.name().trim());
        user.setEmail(email);
        user.setPassword(encoder.encode(req.password()));
        user.setRole(req.role());
        user = users.save(user);

        if (req.role() == Role.CANDIDATE) {
            CandidateProfile p = new CandidateProfile();
            p.setUser(user);
            candidates.save(p);
        } else {
            Company c = new Company();
            c.setUser(user);
            c.setCompanyName(req.companyName() == null || req.companyName().isBlank()
                    ? user.getName() : req.companyName().trim());
            companies.save(c);
        }
        return new AuthResponse(jwt.generateToken(user.getEmail(), user.getRole().name()), toDto(user));
    }

    public AuthResponse login(LoginRequest req) {
        User user = users.findByEmail(req.email().trim().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("bad"));
        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new BadCredentialsException("bad");
        }
        if (!user.isActive()) {
            throw ApiException.forbidden("This account has been blocked. Contact the administrator.");
        }
        return new AuthResponse(jwt.generateToken(user.getEmail(), user.getRole().name()), toDto(user));
    }
}

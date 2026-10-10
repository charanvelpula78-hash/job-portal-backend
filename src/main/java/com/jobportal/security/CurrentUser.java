package com.jobportal.security;

import com.jobportal.exception.ApiException;
import com.jobportal.model.User;
import com.jobportal.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** Helper to get the logged-in User entity inside services/controllers. */
@Component
public class CurrentUser {

    private final UserRepository userRepository;

    public CurrentUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Please log in");
        }
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Please log in again"));
    }

    /** Like get(), but returns null for anonymous visitors instead of throwing. */
    public User getOrNull() {
        try {
            return get();
        } catch (ApiException e) {
            return null;
        }
    }
}

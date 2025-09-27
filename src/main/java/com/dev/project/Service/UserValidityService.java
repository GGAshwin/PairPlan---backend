package com.dev.project.Service;

import org.springframework.stereotype.Service;

import com.dev.project.Entity.UserEntity;
import com.dev.project.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserValidityService {
    private final UserRepository userRepository;

    public UserValidityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity isUserExists(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

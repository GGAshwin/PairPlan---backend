package com.dev.project.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dev.project.DTO.MessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Define excluded paths that don't require JWT authentication
    private final List<String> excludedPaths = Arrays.asList(
            "/api/auth/login",
            "/api/users" // POST to create user
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        String method = request.getMethod();

        // Skip JWT validation for excluded paths
        if (isExcludedPath(requestPath, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, HttpStatus.BAD_REQUEST, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7); // Strip "Bearer "

        try {
            // Validate the token
            String username = jwtUtil.extractName(token);

            // Add username to request attributes for controllers to use
            request.setAttribute("username", username);

            // Continue with the filter chain
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid or expired token!");
        }
    }

    private boolean isExcludedPath(String requestPath, String method) {
        // Check for login endpoint
        if ("/api/auth/login".equals(requestPath)) {
            return true;
        }

        // Check for user creation endpoint (POST to /api/users)
        if ("/api/users".equals(requestPath) && "POST".equalsIgnoreCase(method)) {
            return true;
        }

        return false;
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");

        MessageDTO errorMessage = MessageDTO.error(message);
        String jsonResponse = objectMapper.writeValueAsString(errorMessage);

        response.getWriter().write(jsonResponse);
    }
}

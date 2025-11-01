// src/main/java/com/example/complaintsystembackend/auth/AuthenticationController.java
package com.example.complaint_system_backend.auth;

import com.example.complaint_system_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// DTOs (Data Transfer Objects) for the request and response
class AuthRequest {
    public String username;
    public String password;
}
class AuthResponse {
    public String token;
    public AuthResponse(String token) { this.token = token; }
}

@RestController
@RequestMapping("/api/admin")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.username);
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}
// src/main/java/com/example/complaintsystembackend/config/ApplicationConfig.java
package com.example.complaint_system_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    // --- We moved these beans from SecurityConfig to here ---

    // Bean for the in-memory admin user
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails adminUser = User.builder()
            .username("admin")
            .password("{noop}password") // {noop} means no password encoding
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(adminUser);
    }

    // Bean for the AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // --- Your existing RestTemplate bean (if you had one) can also live here ---
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
package de.trainer.security;


import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final AuthenticationFilter authenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http.cors(cors ->
                cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of(
                            // Zum Testen mit MCP Inspector
                            "http://localhost:6274"
                    ));
                    config.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated());
        return http.build();
    }
}

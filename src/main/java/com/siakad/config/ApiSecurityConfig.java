package com.siakad.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher; // NEW IMPORT
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector; // NEW IMPORT

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final HandlerMappingIntrospector introspector;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // Create MvcRequestMatcher.Builder here
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(registry -> {
                    registry
                            // Allow access to CAPTCHA static files
                            .requestMatchers(mvcMatcherBuilder.pattern("/captcha/**")).permitAll()
                            // Allow access to Swagger UI and OpenAPI docs
                            .requestMatchers(mvcMatcherBuilder.pattern("/api-docs/**")).permitAll() // Corrected path
                            .requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui.html")).permitAll()
                            .requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui/**")).permitAll()
                            // Your existing public API endpoints (e.g., authentication)
                            .requestMatchers(
                                    mvcMatcherBuilder.pattern("/auth/**"), // Example: your login/register endpoints
                                    mvcMatcherBuilder.pattern(HttpMethod.POST, "/users") // Example: allow POST to /users
                                    // Add any other specific public paths here
                            ).permitAll()
                            // All other requests require authentication
                            .anyRequest().authenticated();
                }).sessionManagement(configure -> {
                    configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(configure ->
                        configure.authenticationEntryPoint((request,
                                                            response,
                                                            authException) -> {
                            throw authException;
                        })).build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Ensure you include http://localhost:8080 or your backend port if the frontend is served from there
        configuration.setAllowedOrigins(List.of(
                "http://localhost:8081",
                "http://localhost:5173",
                "https://entirely-dynamic-penguin.ngrok-free.app",
                "http://localhost:8080",
                "https://nl-siak.uika-bogor.ac.id/"
        )); // Added backend host as allowed origin for self-hosted frontend
        configuration.setAllowedMethods(List.of("*")); // Or specify HttpMethod.GET, HttpMethod.POST, etc.
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
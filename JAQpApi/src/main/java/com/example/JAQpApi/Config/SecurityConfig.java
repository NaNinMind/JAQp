package com.example.JAQpApi.Config;

import com.example.JAQpApi.Config.JwtAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};

    private JwtAuthEntryPoint authEntryPoint;
    @Autowired
    public SecurityConfig(JwtAuthEntryPoint authEntryPoint) {
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf( (csrf) -> csrf.disable() )
            .exceptionHandling( (exceptionHandling) -> exceptionHandling.authenticationEntryPoint(authEntryPoint) )
            .sessionManagement( (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
            .authorizeHttpRequests((authorizeHttpRequests) ->
                    authorizeHttpRequests
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/demo-controller/**").permitAll()
                        .anyRequest().authenticated()
                        
            )
            .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JWTAuthFilter jwtAuthFilter() {
        return new JWTAuthFilter();
    }
}

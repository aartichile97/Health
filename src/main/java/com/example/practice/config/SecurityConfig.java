package com.example.practice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	 private final JwtRequestFilter jwtRequestFilter;

	    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
	        this.jwtRequestFilter = jwtRequestFilter;
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.csrf(csrf -> csrf.disable())
	                .authorizeHttpRequests(auth -> auth
	                                .requestMatchers(
	                                		 "/users/register",
	                                		    "/users/login",
	                                		    "/swagger-ui/**",
	                                		    "/swagger-ui.html",
	                                		    "/v3/api-docs/**",
	                                		    "/swagger-resources/**",
	                                		    "/webjars/**",
	                                		    "/v3/api-docs"
	                                ).permitAll()
	                                .anyRequest().authenticated()
	                )
	                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	    }
}

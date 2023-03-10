package com.hospital.management.config;

import com.hospital.management.service.implementation.UserDetailsServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl jwtUserDetailsService;
    private final JwtToken jwtToken;

    public JwtRequestFilter(UserDetailsServiceImpl jwtUserDetailsService, JwtToken jwtToken) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtToken = jwtToken;
    }


    //TODO continue with doFilterInternal
}

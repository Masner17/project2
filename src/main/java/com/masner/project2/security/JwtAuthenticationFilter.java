package com.masner.project2.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService){
        this.jwtService = jwtService;
    }
    @Override
        protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        //Leer el header
        String authHeader = request.getHeader("Authorization");

        //Si no viene o no empieza con Bearer -> seguir sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String userEmail;

        try {
            userEmail = jwtService.extractEmail(jwt);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

    String role = jwtService.extractRole(jwt);
    List<GrantedAuthority> authorities =
        List.of(new SimpleGrantedAuthority("ROLE_" + role));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        authorities 
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}

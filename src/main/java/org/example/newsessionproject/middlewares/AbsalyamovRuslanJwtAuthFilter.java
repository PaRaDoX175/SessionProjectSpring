package org.example.newsessionproject.middlewares;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.newsessionproject.dtos.AbsalyamovRuslanUserDetails;
import org.example.newsessionproject.services.AbsalyamovRuslanJwtService;
import org.example.newsessionproject.services.AbsalyamovRuslanUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AbsalyamovRuslanJwtAuthFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanJwtAuthFilter.class);
    private final AbsalyamovRuslanUserDetailsService userDetailsService;
    private final AbsalyamovRuslanJwtService jwtService;

    public AbsalyamovRuslanJwtAuthFilter(AbsalyamovRuslanUserDetailsService userDetailsService,
                                         AbsalyamovRuslanJwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var auth = request.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            log.debug("JWT has missing or invalid Authorization header for path={}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        var token = auth.substring(7);
        var email = jwtService.getEmailFromToken(token);
        var id = jwtService.getIdFromToken(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailsService.loadUserByUsername(email);

            AbsalyamovRuslanUserDetails cud = new AbsalyamovRuslanUserDetails(id,
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    userDetails.getAuthorities());

            if (jwtService.isEmailCorrect(email, userDetails)) {
                var authToken = new UsernamePasswordAuthenticationToken(cud, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("JWT authentication applied for userId={} path={}", id, request.getRequestURI());
            } else {
                log.warn("JWT email mismatch for path={}", request.getRequestURI());
            }
        }

        filterChain.doFilter(request, response);
    }
}

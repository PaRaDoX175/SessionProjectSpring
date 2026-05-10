package org.example.newsessionproject.middlewares;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.newsessionproject.dtos.AbsalyamovRuslanUserDetails;
import org.example.newsessionproject.services.AbsalyamovRuslanJwtService;
import org.example.newsessionproject.services.AbsalyamovRuslanUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class AbsalyamovRuslanJwtAuthFilter extends OncePerRequestFilter {
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
            }
        }

        filterChain.doFilter(request, response);
    }
}

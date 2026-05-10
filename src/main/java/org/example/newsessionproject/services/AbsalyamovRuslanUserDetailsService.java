package org.example.newsessionproject.services;

import org.example.newsessionproject.repositories.AbsalyamovRuslanUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class AbsalyamovRuslanUserDetailsService implements UserDetailsService {

    private final AbsalyamovRuslanUserRepository userRepository;

    public AbsalyamovRuslanUserDetailsService(AbsalyamovRuslanUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return null;
    }
}

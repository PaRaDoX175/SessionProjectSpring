package org.example.newsessionproject.services;

import org.example.newsessionproject.exceptions.AbsalyamovRuslanNotFoundException;
import org.example.newsessionproject.repositories.AbsalyamovRuslanUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbsalyamovRuslanUserDetailsService implements UserDetailsService {

    private final AbsalyamovRuslanUserRepository userRepository;

    public AbsalyamovRuslanUserDetailsService(AbsalyamovRuslanUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("User with this email is not found!"));

        return new User(user.getEmail(), user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name())));
    }
}

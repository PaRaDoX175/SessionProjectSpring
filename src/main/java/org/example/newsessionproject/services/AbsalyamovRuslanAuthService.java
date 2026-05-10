package org.example.newsessionproject.services;

import org.example.newsessionproject.dtos.*;
import org.example.newsessionproject.entities.AbsalyamovRuslanClient;
import org.example.newsessionproject.entities.AbsalyamovRuslanFreelancer;
import org.example.newsessionproject.entities.AbsalyamovRuslanUser;
import org.example.newsessionproject.enums.AbsalyamovRuslanRoleName;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanNotFoundException;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanUserAlreadyExistException;
import org.example.newsessionproject.repositories.AbsalyamovRuslanClientRepository;
import org.example.newsessionproject.repositories.AbsalyamovRuslanFreelancerRepository;
import org.example.newsessionproject.repositories.AbsalyamovRuslanUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AbsalyamovRuslanAuthService {
    private final AbsalyamovRuslanUserRepository userRepository;
    private final AbsalyamovRuslanJwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AbsalyamovRuslanFreelancerRepository freelancerRepository;
    private final AbsalyamovRuslanClientRepository clientRepository;

    public AbsalyamovRuslanAuthService(AbsalyamovRuslanUserRepository userRepository,
                       AbsalyamovRuslanJwtService jwtService,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       AbsalyamovRuslanFreelancerRepository freelancerRepository,
                       AbsalyamovRuslanClientRepository clientRepository
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.freelancerRepository = freelancerRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public AbsalyamovRuslanJWT register(AbsalyamovRuslanFreelancerRegister register) {

        if (userRepository.existsByEmail(register.getEmail())) {
            throw new AbsalyamovRuslanUserAlreadyExistException("User with this email is already exist!");
        }

        var user = createUser(register);
        user.setRole(AbsalyamovRuslanRoleName.ROLE_FREELANCER);

        var freelancer = new AbsalyamovRuslanFreelancer(register.getName(), register.getSurname());
        freelancer.setUser(user);

        userRepository.save(user);

        freelancerRepository.save(freelancer);

        return new AbsalyamovRuslanJWT(jwtService.generateToken(user));
    }

    @Transactional
    public AbsalyamovRuslanJWT register(AbsalyamovRuslanClientRegister register) {

        if (userRepository.existsByEmail(register.getEmail())) {
            throw new AbsalyamovRuslanUserAlreadyExistException("User with this email is already exist!");
        }

        var user = createUser(register);
        user.setRole(AbsalyamovRuslanRoleName.ROLE_CLIENT);
        var client = new AbsalyamovRuslanClient(register.getCompanyName(), register.getCompanyDescription());
        client.setUser(user);

        userRepository.save(user);
        clientRepository.save(client);

        return new AbsalyamovRuslanJWT(jwtService.generateToken(user));
    }

    public AbsalyamovRuslanJWT login(AbsalyamovRuslanLogin login) {

        var user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("User with this email is not found!"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));

        return new AbsalyamovRuslanJWT(jwtService.generateToken(user));
    }

    private AbsalyamovRuslanUser createUser(AbsalyamovRuslanRegister register) {
        return new AbsalyamovRuslanUser(register.getEmail(), passwordEncoder.encode(register.getPassword()));
    }
}

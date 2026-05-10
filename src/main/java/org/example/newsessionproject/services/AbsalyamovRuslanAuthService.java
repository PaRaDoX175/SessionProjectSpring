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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AbsalyamovRuslanAuthService {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanAuthService.class);
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
        log.info("Freelancer registration started");

        if (userRepository.existsByEmail(register.getEmail())) {
            log.warn("Freelancer registration rejected because email already exists");
            throw new AbsalyamovRuslanUserAlreadyExistException("User with this email is already exist!");
        }

        var user = createUser(register);
        user.setRole(AbsalyamovRuslanRoleName.ROLE_FREELANCER);

        var freelancer = new AbsalyamovRuslanFreelancer(register.getName(), register.getSurname());
        freelancer.setUser(user);

        userRepository.save(user);

        freelancerRepository.save(freelancer);
        log.info("Freelancer registration completed for userId={}", user.getId());

        return new AbsalyamovRuslanJWT(jwtService.generateToken(user));
    }

    @Transactional
    public AbsalyamovRuslanJWT register(AbsalyamovRuslanClientRegister register) {
        log.info("Client registration started");

        if (userRepository.existsByEmail(register.getEmail())) {
            log.warn("Client registration rejected because email already exists");
            throw new AbsalyamovRuslanUserAlreadyExistException("User with this email is already exist!");
        }

        var user = createUser(register);
        user.setRole(AbsalyamovRuslanRoleName.ROLE_CLIENT);
        var client = new AbsalyamovRuslanClient(register.getCompanyName(), register.getCompanyDescription());
        client.setUser(user);

        userRepository.save(user);
        clientRepository.save(client);
        log.info("Client registration completed for userId={}", user.getId());

        return new AbsalyamovRuslanJWT(jwtService.generateToken(user));
    }

    public AbsalyamovRuslanJWT login(AbsalyamovRuslanLogin login) {
        log.info("Login started");

        var user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed because user not found");
                    return new AbsalyamovRuslanNotFoundException("User with this email is not found!");
                });

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        log.info("Login succeeded for userId={}", user.getId());

        return new AbsalyamovRuslanJWT(jwtService.generateToken(user));
    }

    private AbsalyamovRuslanUser createUser(AbsalyamovRuslanRegister register) {
        return new AbsalyamovRuslanUser(register.getEmail(), passwordEncoder.encode(register.getPassword()));
    }
}

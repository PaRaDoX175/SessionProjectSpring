package org.example.newsessionproject.controllers;

import jakarta.validation.Valid;
import org.example.newsessionproject.dtos.AbsalyamovRuslanClientRegister;
import org.example.newsessionproject.dtos.AbsalyamovRuslanFreelancerRegister;
import org.example.newsessionproject.dtos.AbsalyamovRuslanJWT;
import org.example.newsessionproject.dtos.AbsalyamovRuslanLogin;
import org.example.newsessionproject.services.AbsalyamovRuslanAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AbsalyamovRuslanAuthController {
    private final AbsalyamovRuslanAuthService authService;

    public AbsalyamovRuslanAuthController(AbsalyamovRuslanAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/freelancer")
    public AbsalyamovRuslanJWT registerFreelancer(@RequestBody @Valid AbsalyamovRuslanFreelancerRegister freelancerRegister) {
        return authService.register(freelancerRegister);
    }

    @PostMapping("/register/client")
    public AbsalyamovRuslanJWT registerClient(@RequestBody @Valid AbsalyamovRuslanClientRegister clientRegister) {
        return authService.register(clientRegister);
    }

    @PostMapping("/login")
    public AbsalyamovRuslanJWT login(@RequestBody @Valid AbsalyamovRuslanLogin login) {
        return authService.login(login);
    }
}

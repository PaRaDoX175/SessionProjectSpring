package org.example.newsessionproject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Registration and login endpoints")
public class AbsalyamovRuslanAuthController {
    private final AbsalyamovRuslanAuthService authService;

    public AbsalyamovRuslanAuthController(AbsalyamovRuslanAuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register as freelancer",
            description = "Creates a new freelancer account and returns JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully registered"),
            @ApiResponse(responseCode = "400", description = "User already exists", content = @Content)
    })
    @PostMapping("/register/freelancer")
    public AbsalyamovRuslanJWT registerFreelancer(@RequestBody @Valid AbsalyamovRuslanFreelancerRegister freelancerRegister) {
        return authService.register(freelancerRegister);
    }

    @Operation(
            summary = "Register as client",
            description = "Creates a new client account and returns JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully registered"),
            @ApiResponse(responseCode = "400", description = "User already exists", content = @Content)
    })
    @PostMapping("/register/client")
    public AbsalyamovRuslanJWT registerClient(@RequestBody @Valid AbsalyamovRuslanClientRegister clientRegister) {
        return authService.register(clientRegister);
    }

    @Operation(
            summary = "Login",
            description = "Authenticates user and returns JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                    content = @Content(schema = @Schema(implementation = AbsalyamovRuslanJWT.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "There is no user with this email", content = @Content)
    })
    @PostMapping("/login")
    public AbsalyamovRuslanJWT login(@RequestBody @Valid AbsalyamovRuslanLogin login) {
        return authService.login(login);
    }
}

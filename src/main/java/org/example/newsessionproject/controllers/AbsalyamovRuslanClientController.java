package org.example.newsessionproject.controllers;

import jakarta.validation.Valid;
import org.example.newsessionproject.dtos.AbsalyamovRuslanAddVacancyDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanClientResponseDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanUserDetails;
import org.example.newsessionproject.services.AbsalyamovRuslanClientService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
public class AbsalyamovRuslanClientController {
    private final AbsalyamovRuslanClientService clientService;

    public AbsalyamovRuslanClientController(AbsalyamovRuslanClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public AbsalyamovRuslanClientResponseDto getVacancies(@AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        return clientService.getVacancies(user.getId());
    }

    @PatchMapping
    @PreAuthorize("hasRole('CLIENT')")
    public AbsalyamovRuslanClientResponseDto addVacancy(@RequestBody @Valid AbsalyamovRuslanAddVacancyDto dto,
                                        @AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        return clientService.addVacancy(user.getId(), dto);
    }
}

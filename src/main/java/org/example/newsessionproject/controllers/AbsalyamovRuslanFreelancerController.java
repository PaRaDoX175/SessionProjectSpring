package org.example.newsessionproject.controllers;

import jakarta.validation.Valid;
import org.example.newsessionproject.dtos.AbsalyamovRuslanAddResumeDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanFreelancerResponseDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanUserDetails;
import org.example.newsessionproject.services.AbsalyamovRuslanFreelancerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/freelancer")
public class AbsalyamovRuslanFreelancerController {
    private final AbsalyamovRuslanFreelancerService freelancerService;

    public AbsalyamovRuslanFreelancerController(AbsalyamovRuslanFreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    @GetMapping
    public AbsalyamovRuslanFreelancerResponseDto getFreelancer(@AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        return freelancerService.getFreelancer(user.getId());
    }

    @PatchMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public AbsalyamovRuslanFreelancerResponseDto addResume(@RequestBody @Valid AbsalyamovRuslanAddResumeDto dto,
                                           @AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        return freelancerService.addResume(user.getId(), dto);
    }
}

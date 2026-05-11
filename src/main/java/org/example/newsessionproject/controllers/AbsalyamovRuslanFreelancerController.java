package org.example.newsessionproject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.newsessionproject.dtos.AbsalyamovRuslanAddResumeDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanFreelancerResponseDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanUserDetails;
import org.example.newsessionproject.services.AbsalyamovRuslanFreelancerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/freelancer")
@Tag(name = "Freelancer", description = "Freelancer profile and resume management endpoints")
public class AbsalyamovRuslanFreelancerController {
    private final AbsalyamovRuslanFreelancerService freelancerService;

    public AbsalyamovRuslanFreelancerController(AbsalyamovRuslanFreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    @Operation(
            summary = "Get freelancer profile",
            description = "Returns the profile of the authenticated freelancer."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Freelancer profile not found", content = @Content)
    })
    @GetMapping
    public AbsalyamovRuslanFreelancerResponseDto getFreelancer(@AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        return freelancerService.getFreelancer(user.getId());
    }

    @Operation(
            summary = "Add or update resume",
            description = "Adds or updates the resume of the authenticated freelancer."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resume updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Freelancer profile not found", content = @Content)
    })
    @PatchMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public AbsalyamovRuslanFreelancerResponseDto addResume(@RequestBody @Valid AbsalyamovRuslanAddResumeDto dto,
                                           @AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        return freelancerService.addResume(user.getId(), dto);
    }

    @Operation(
            summary = "Delete resume",
            description = "Delete the resume of the authenticated freelancer."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Resume successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Resume not found", content = @Content)
    })
    @DeleteMapping("/{resumeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResume(@PathVariable Long resumeId,
                             @AuthenticationPrincipal AbsalyamovRuslanUserDetails userDetails) {
        freelancerService.deleteResume(userDetails.getId(), resumeId);
    }
}

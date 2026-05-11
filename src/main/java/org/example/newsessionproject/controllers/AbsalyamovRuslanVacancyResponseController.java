package org.example.newsessionproject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.newsessionproject.dtos.AbsalyamovRuslanAddVacancyResponseDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanChangeProposalStatusDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanUserDetails;
import org.example.newsessionproject.entities.AbsalyamovRuslanVacancyResponse;
import org.example.newsessionproject.services.AbsalyamovRuslanVacancyResponseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacancyResponse")
@Tag(name = "Vacancy Response", description = "Endpoints for managing responses to vacancies")
public class AbsalyamovRuslanVacancyResponseController {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanVacancyResponseController.class);
    private final AbsalyamovRuslanVacancyResponseService vacancyResponseService;

    public AbsalyamovRuslanVacancyResponseController(AbsalyamovRuslanVacancyResponseService vacancyResponseService) {
        this.vacancyResponseService = vacancyResponseService;
    }

    @Operation(
            summary = "Get responses for vacancy",
            description = "Returns all freelancer responses for a specific vacancy. Accessible only to clients."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Responses retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied. CLIENT role required", content = @Content),
            @ApiResponse(responseCode = "404", description = "Vacancy not found", content = @Content)
    })
    @GetMapping("/vacancy/{vacancyId}")
    @PreAuthorize("hasRole('CLIENT')")
    public List<AbsalyamovRuslanVacancyResponse> getResponsesForClient(@PathVariable Long vacancyId) {
        log.debug("API getResponsesForClient called vacancyId={}", vacancyId);
        return vacancyResponseService.getResponsesForClient(vacancyId);
    }

    @Operation(
            summary = "Get my responses",
            description = "Returns all vacancy responses submitted by the authenticated freelancer."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Responses retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied. FREELANCER role required", content = @Content)
    })
    @GetMapping("/freelancer")
    @PreAuthorize("hasRole('FREELANCER')")
    public List<AbsalyamovRuslanVacancyResponse> getResponsesForFreelancer(@AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        log.debug("API getResponsesForFreelancer called userId={}", user.getId());
        return vacancyResponseService.getResponsesForFreelancer(user.getId());
    }

    @Operation(
            summary = "Apply for vacancy",
            description = "Submits a response to a vacancy on behalf of the authenticated freelancer."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Response submitted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied. FREELANCER role required", content = @Content),
            @ApiResponse(responseCode = "404", description = "Vacancy or freelancer not found", content = @Content)
    })
    @PostMapping("/{vacId}")
    @PreAuthorize("hasRole('FREELANCER')")
    public void addResponse(@PathVariable Long vacId,
                            @AuthenticationPrincipal AbsalyamovRuslanUserDetails userDetails) {
        log.info("API addResponse called vacancyId={} userId={}", vacId, userDetails.getId());
        vacancyResponseService.addResponse(vacId, userDetails.getId());
    }

    @Operation(
            summary = "Change proposal status",
            description = "Updates the status of a freelancer's vacancy response. Accessible only to clients."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied. CLIENT role required", content = @Content),
            @ApiResponse(responseCode = "404", description = "Response not found", content = @Content)
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public AbsalyamovRuslanVacancyResponse changeProposalStatus(@PathVariable Long id,
                                                                @RequestBody @Valid AbsalyamovRuslanChangeProposalStatusDto dto) {
        log.info("API changeProposalStatus called responseId={} status={}", id, dto.getStatus());
        return vacancyResponseService.changeProposalStatus(id, dto);
    }
}

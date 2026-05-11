package org.example.newsessionproject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.newsessionproject.dtos.AbsalyamovRuslanAddVacancyDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanClientResponseDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanUserDetails;
import org.example.newsessionproject.services.AbsalyamovRuslanClientService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
@Tag(name = "Client controller", description = "Client controller endpoints")
public class AbsalyamovRuslanClientController {
    private final AbsalyamovRuslanClientService clientService;

    public AbsalyamovRuslanClientController(AbsalyamovRuslanClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Get vacancies by user id", description = "returns all company vacancies")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vacancies retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public AbsalyamovRuslanClientResponseDto getVacancies(@AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        return clientService.getVacancies(user.getId());
    }

    @Operation(summary = "Add vacancy", description = "Add vacancy to company by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vacancy successfully added"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @PatchMapping
    @PreAuthorize("hasRole('CLIENT')")
    public AbsalyamovRuslanClientResponseDto addVacancy(@RequestBody @Valid AbsalyamovRuslanAddVacancyDto dto,
                                        @AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        return clientService.addVacancy(user.getId(), dto);
    }

    @Operation(summary = "Delete vacancy", description = "Delete vacancy from client")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted"),
            @ApiResponse(responseCode = "403", description = "Access for deletion is denied")
    })
    @DeleteMapping("/{vacId}")
    @PreAuthorize("hasRole('CLIENT')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVacancy(@PathVariable Long vacId,
                              @AuthenticationPrincipal AbsalyamovRuslanUserDetails userDetails) {
        clientService.deleteVacancy(userDetails.getId(), vacId);
    }
}

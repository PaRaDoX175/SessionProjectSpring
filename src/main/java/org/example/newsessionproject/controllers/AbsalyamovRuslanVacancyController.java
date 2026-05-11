package org.example.newsessionproject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.newsessionproject.dtos.AbsalyamovRuslanVacancyResponseDto;
import org.example.newsessionproject.services.AbsalyamovRuslanVacancyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vacancy")
@Tag(name = "Vacancy", description = "Vacancy search and management endpoints")
public class AbsalyamovRuslanVacancyController {
    private final AbsalyamovRuslanVacancyService vacancyService;

    public AbsalyamovRuslanVacancyController(AbsalyamovRuslanVacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @Operation(
            summary = "Get vacancies",
            description = "Returns a paginated and searchable list of vacancies. Accessible only to freelancers."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vacancies retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied. FREELANCER role required", content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public List<AbsalyamovRuslanVacancyResponseDto> getVacancies(
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "2") int pageSize,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        return vacancyService.getVacancies(pageIndex, pageSize, search, sortBy, dir);
    }
}

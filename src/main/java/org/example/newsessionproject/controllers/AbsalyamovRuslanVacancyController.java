package org.example.newsessionproject.controllers;

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
public class AbsalyamovRuslanVacancyController {
    private final AbsalyamovRuslanVacancyService vacancyService;

    public AbsalyamovRuslanVacancyController(AbsalyamovRuslanVacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

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

package org.example.newsessionproject.controllers;

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
public class AbsalyamovRuslanVacancyResponseController {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanVacancyResponseController.class);
    private final AbsalyamovRuslanVacancyResponseService vacancyResponseService;

    public AbsalyamovRuslanVacancyResponseController(AbsalyamovRuslanVacancyResponseService vacancyResponseService) {
        this.vacancyResponseService = vacancyResponseService;
    }

    @GetMapping("/vacancy/{vacancyId}")
    @PreAuthorize("hasRole('CLIENT')")
    public List<AbsalyamovRuslanVacancyResponse> getResponsesForClient(@PathVariable Long vacancyId) {
        log.debug("API getResponsesForClient called vacancyId={}", vacancyId);
        return vacancyResponseService.getResponsesForClient(vacancyId);
    }

    @GetMapping("/freelancer")
    @PreAuthorize("hasRole('FREELANCER')")
    public List<AbsalyamovRuslanVacancyResponse> getResponsesForFreelancer(@AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        log.debug("API getResponsesForFreelancer called userId={}", user.getId());
        return vacancyResponseService.getResponsesForFreelancer(user.getId());
    }

    @PostMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public void addResponse(@RequestBody @Valid AbsalyamovRuslanAddVacancyResponseDto dto) {
        log.info("API addResponse called vacancyId={} freelancerId={}", dto.getVacancyId(), dto.getFreelancerId());
        vacancyResponseService.addResponse(dto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public AbsalyamovRuslanVacancyResponse changeProposalStatus(@PathVariable Long id,
                                                                @RequestBody @Valid AbsalyamovRuslanChangeProposalStatusDto dto) {
        log.info("API changeProposalStatus called responseId={} status={}", id, dto.getStatus());
        return vacancyResponseService.changeProposalStatus(id, dto);
    }
}

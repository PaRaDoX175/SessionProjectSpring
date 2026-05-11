package org.example.newsessionproject.services;

import org.example.newsessionproject.dtos.AbsalyamovRuslanAddVacancyResponseDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanChangeProposalStatusDto;
import org.example.newsessionproject.entities.AbsalyamovRuslanVacancyResponse;
import org.example.newsessionproject.enums.AbsalyamovRuslanProposalStatus;
import org.example.newsessionproject.enums.AbsalyamovRuslanRoleName;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanAccessDeniedException;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanNotFoundException;
import org.example.newsessionproject.repositories.AbsalyamovRuslanFreelancerRepository;
import org.example.newsessionproject.repositories.AbsalyamovRuslanUserRepository;
import org.example.newsessionproject.repositories.AbsalyamovRuslanVacancyRepository;
import org.example.newsessionproject.repositories.AbsalyamovRuslanVacancyResponseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbsalyamovRuslanVacancyResponseService {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanVacancyResponseService.class);
    private final AbsalyamovRuslanVacancyRepository vacancyRepository;
    private final AbsalyamovRuslanFreelancerRepository freelancerRepository;
    private final AbsalyamovRuslanVacancyResponseRepository vacancyResponseRepository;
    private final AbsalyamovRuslanChatService chatService;
    private final AbsalyamovRuslanEmailService emailService;
    private final AbsalyamovRuslanUserRepository userRepository;

    public AbsalyamovRuslanVacancyResponseService(AbsalyamovRuslanVacancyRepository vacancyRepository,
                                  AbsalyamovRuslanFreelancerRepository freelancerRepository,
                                  AbsalyamovRuslanVacancyResponseRepository vacancyResponseRepository,
                                  AbsalyamovRuslanChatService chatService,
                                  AbsalyamovRuslanEmailService emailService,
                                  AbsalyamovRuslanUserRepository userRepository) {
        this.vacancyRepository = vacancyRepository;
        this.freelancerRepository = freelancerRepository;
        this.vacancyResponseRepository = vacancyResponseRepository;
        this.chatService = chatService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public List<AbsalyamovRuslanVacancyResponse> getResponsesForClient(Long vacancyId) {
        var responses = vacancyResponseRepository.findAllByVacancy_Id(vacancyId);
        log.info("Loaded {} responses for vacancyId={}", responses.size(), vacancyId);
        return responses;
    }

    public List<AbsalyamovRuslanVacancyResponse> getResponsesForFreelancer(Long userId) {
        log.debug("Loading responses for freelancer userId={}", userId);
        var u = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found for responses lookup userId={}", userId);
                    return new AbsalyamovRuslanNotFoundException("User with this id is not found!");
                });

        if (u.getRole() != AbsalyamovRuslanRoleName.ROLE_FREELANCER) {
            log.warn("Access denied for responses lookup userId={} role={}", userId, u.getRole());
            throw new AbsalyamovRuslanAccessDeniedException("Only freelancer can do it!");
        }

        var freelancer = freelancerRepository.findByUserId(u.getId())
                .orElseThrow(() -> {
                    log.warn("Freelancer profile not found for userId={}", u.getId());
                    return new AbsalyamovRuslanNotFoundException("Freelancer with this id is not found!");
                });

        var responses = vacancyResponseRepository.findAllByFreelancerProfile_Id(freelancer.getId());
        log.info("Loaded {} responses for freelancerProfileId={}", responses.size(), freelancer.getId());
        return responses;
    }


    public void addResponse(Long vacancyId, Long userId) {
        log.info("Adding vacancy response vacancyId={} userId={}", vacancyId, userId);
        var vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> {
                    log.warn("Vacancy not found vacancyId={}", vacancyId);
                    return new AbsalyamovRuslanNotFoundException("Vacancy with this id is not found!");
                });

        var freelancer = freelancerRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Freelancer not found userId={}", userId);
                    return new AbsalyamovRuslanNotFoundException("Freelancer with this id is not found!");
                });

        var vacResp = new AbsalyamovRuslanVacancyResponse(freelancer, vacancy);
        vacancyResponseRepository.save(vacResp);
        log.info("Vacancy response created responseId={}", vacResp.getId());

        emailService.send(vacancy.getClientProfile().getUser().getEmail(), "Your vacancy has been responded to!",
                "It was this user: " + freelancer.getName() + " " + freelancer.getSurname());
        log.debug("Response notification email queued for responseId={}", vacResp.getId());
    }


    public AbsalyamovRuslanVacancyResponse changeProposalStatus(Long id, AbsalyamovRuslanChangeProposalStatusDto dto) {
        log.info("Changing proposal status responseId={} newStatus={}", id, dto.getStatus());
        var vacResp = vacancyResponseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Vacancy response not found responseId={}", id);
                    return new AbsalyamovRuslanNotFoundException("This vacancy response is not found by this id!");
                });

        vacResp.setProposalStatus(dto.getStatus());

        if (dto.getStatus().equals(AbsalyamovRuslanProposalStatus.ACCEPTED)) {
            var chat = chatService.createChat(
                    vacResp.getVacancy().getClientProfile().getId(),
                    vacResp.getFreelancerProfile().getId(),
                    vacResp.getVacancy().getId(),
                    vacResp.getId());
            log.info("Chat created chatId={} for accepted responseId={}", chat.getId(), vacResp.getId());

            emailService.send(vacResp.getFreelancerProfile().getUser().getEmail(), "Client start chat with you!",
                    "Client from this company: " + vacResp.getVacancy().getClientProfile().getCompanyName()
                            + " from this vacancy:" + vacResp.getVacancy() + " wants to work with you");
            log.debug("Chat invitation email queued for responseId={}", vacResp.getId());
        }

        vacancyResponseRepository.save(vacResp);
        log.info("Proposal status updated responseId={} status={}", vacResp.getId(), vacResp.getProposalStatus());
        return vacResp;
    }
}

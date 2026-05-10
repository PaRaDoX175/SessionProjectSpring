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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbsalyamovRuslanVacancyResponseService {
    private final AbsalyamovRuslanVacancyRepository vacancyRepository;
    private final AbsalyamovRuslanFreelancerRepository freelancerRepository;
    private final AbsalyamovRuslanVacancyResponseRepository vacancyResponseRepository;
    private final AbsalyamovRuslanChatService chatService;
    private final EmailService emailService;
    private final AbsalyamovRuslanUserRepository userRepository;

    public AbsalyamovRuslanVacancyResponseService(AbsalyamovRuslanVacancyRepository vacancyRepository,
                                  AbsalyamovRuslanFreelancerRepository freelancerRepository,
                                  AbsalyamovRuslanVacancyResponseRepository vacancyResponseRepository,
                                  AbsalyamovRuslanChatService chatService,
                                  EmailService emailService,
                                  AbsalyamovRuslanUserRepository userRepository) {
        this.vacancyRepository = vacancyRepository;
        this.freelancerRepository = freelancerRepository;
        this.vacancyResponseRepository = vacancyResponseRepository;
        this.chatService = chatService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public List<AbsalyamovRuslanVacancyResponse> getResponsesForClient(Long vacancyId) {
        return vacancyResponseRepository.findAllByVacancy_Id(vacancyId);
    }

    public List<AbsalyamovRuslanVacancyResponse> getResponsesForFreelancer(Long userId) {
        var u = userRepository.findById(userId)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("User with this id is not found!"));

        if (u.getRole() != AbsalyamovRuslanRoleName.ROLE_FREELANCER) {
            throw new AbsalyamovRuslanAccessDeniedException("Only freelancer can do it!");
        }

        var freelancer = freelancerRepository.findByUserId(u.getId())
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("Freelancer with this id is not found!"));

        return vacancyResponseRepository.findAllByFreelancerProfile_Id(freelancer.getId());
    }


    public void addResponse(AbsalyamovRuslanAddVacancyResponseDto dto) {
        var vacancy = vacancyRepository.findById(dto.getVacancyId())
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("Vacancy with this id is not found!"));

        var freelancer = freelancerRepository.findById(dto.getFreelancerId())
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("Freelancer with this id is not found!"));

        var vacResp = new AbsalyamovRuslanVacancyResponse(freelancer, vacancy);
        vacancyResponseRepository.save(vacResp);

        emailService.send(vacancy.getClientProfile().getUser().getEmail(), "Your vacancy has been responded to!",
                "It was this user: " + freelancer.getName() + " " + freelancer.getSurname());
    }


    public AbsalyamovRuslanVacancyResponse changeProposalStatus(Long id, AbsalyamovRuslanChangeProposalStatusDto dto) {
        var vacResp = vacancyResponseRepository.findById(id)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("This vacancy response is not found by this id!"));

        vacResp.setProposalStatus(dto.getStatus());

        if (dto.getStatus().equals(AbsalyamovRuslanProposalStatus.ACCEPTED)) {
            chatService.createChat(
                    vacResp.getVacancy().getClientProfile().getId(),
                    vacResp.getFreelancerProfile().getId(),
                    vacResp.getVacancy().getId(),
                    vacResp.getId());

            emailService.send(vacResp.getFreelancerProfile().getUser().getEmail(), "Client start chat with you!",
                    "Client from this company: " + vacResp.getVacancy().getClientProfile().getCompanyName()
                            + " from this vacancy:" + vacResp.getVacancy() + " wants to work with you");
        }

        vacancyResponseRepository.save(vacResp);
        return vacResp;
    }
}

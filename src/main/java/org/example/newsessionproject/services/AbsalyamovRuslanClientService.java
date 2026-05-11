package org.example.newsessionproject.services;

import org.example.newsessionproject.dtos.AbsalyamovRuslanAddVacancyDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanClientResponseDto;
import org.example.newsessionproject.entities.AbsalyamovRuslanVacancy;
import org.example.newsessionproject.enums.AbsalyamovRuslanVacancyCategory;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanAccessDeniedException;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanNotFoundException;
import org.example.newsessionproject.mappers.AbsalyamovRuslanClientMapper;
import org.example.newsessionproject.mappers.AbsalyamovRuslanVacancyMapper;
import org.example.newsessionproject.repositories.AbsalyamovRuslanClientRepository;
import org.example.newsessionproject.repositories.AbsalyamovRuslanVacancyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AbsalyamovRuslanClientService {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanClientService.class);
    private final AbsalyamovRuslanClientRepository clientRepository;
    private final AbsalyamovRuslanVacancyRepository vacancyRepository;
    private final AbsalyamovRuslanClientMapper clientMapper;
    private final AbsalyamovRuslanVacancyMapper vacancyMapper;

    public AbsalyamovRuslanClientService(AbsalyamovRuslanClientRepository clientRepository,
                                         AbsalyamovRuslanVacancyRepository vacancyRepository,
                                         AbsalyamovRuslanClientMapper clientMapper,
                                         AbsalyamovRuslanVacancyMapper vacancyMapper) {
        this.clientRepository = clientRepository;
        this.vacancyRepository = vacancyRepository;
        this.clientMapper = clientMapper;
        this.vacancyMapper = vacancyMapper;
    }

    public AbsalyamovRuslanClientResponseDto getVacancies(Long userId) {
        log.debug("Loading client vacancies for userId={}", userId);
        var clientProfile = clientRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Client profile not found for userId={}", userId);
                    return new AbsalyamovRuslanNotFoundException("client with this id is not found!");
                });

        log.info("Client vacancies loaded for userId={} count={}", userId, clientProfile.getVacancies().size());
        return clientMapper.toDto(clientProfile);
    }

    public AbsalyamovRuslanClientResponseDto addVacancy(Long clientId, AbsalyamovRuslanAddVacancyDto dto) {
        log.info("Adding vacancy for clientId={} position={}", clientId, dto.getPosition());

        var vacancy = vacancyMapper.toEntity(dto);

        var profile = clientRepository.findByUserId(clientId)
                .orElseThrow(() -> {
                    log.warn("Client profile not found for userId={}", clientId);
                    return new AbsalyamovRuslanNotFoundException("client with this id is not found!");
                });

        profile.addVacancy(vacancy);
        vacancy.setClientProfile(profile);

        clientRepository.save(profile);

        log.info("Vacancy added for clientId={} totalVacancies={}", clientId, profile.getVacancies().size());
        return clientMapper.toDto(profile);
    }

    public void deleteVacancy(Long clientId, Long vacancyId) {
        var vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("Vacancy not found"));

        if (!Objects.equals(vacancy.getClientProfile().getId(), clientId)) {
            throw new AbsalyamovRuslanAccessDeniedException("You are not allowed to delete this vacancy");
        }

        vacancyRepository.delete(vacancy);
    }
}

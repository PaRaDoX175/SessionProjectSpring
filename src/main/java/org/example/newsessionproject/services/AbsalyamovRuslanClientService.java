package org.example.newsessionproject.services;

import org.example.newsessionproject.dtos.AbsalyamovRuslanAddVacancyDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanClientResponseDto;
import org.example.newsessionproject.entities.AbsalyamovRuslanVacancy;
import org.example.newsessionproject.enums.AbsalyamovRuslanVacancyCategory;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanNotFoundException;
import org.example.newsessionproject.repositories.AbsalyamovRuslanClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AbsalyamovRuslanClientService {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanClientService.class);
    private final AbsalyamovRuslanClientRepository clientRepository;

    public AbsalyamovRuslanClientService(AbsalyamovRuslanClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public AbsalyamovRuslanClientResponseDto getVacancies(Long userId) {
        log.debug("Loading client vacancies for userId={}", userId);
        var clientProfile = clientRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Client profile not found for userId={}", userId);
                    return new AbsalyamovRuslanNotFoundException("client with this id is not found!");
                });

        var clientResponseDto = new AbsalyamovRuslanClientResponseDto();
        clientResponseDto.setCompanyName(clientProfile.getCompanyName());
        clientResponseDto.setCompanyDescription(clientProfile.getCompanyDescription());
        clientResponseDto.setVacancies(clientProfile.getVacancies());

        log.info("Client vacancies loaded for userId={} count={}", userId, clientProfile.getVacancies().size());
        return clientResponseDto;
    }

    public AbsalyamovRuslanClientResponseDto addVacancy(Long clientId, AbsalyamovRuslanAddVacancyDto dto) {
        log.info("Adding vacancy for clientId={} position={}", clientId, dto.getPosition());

        var vacancy = new AbsalyamovRuslanVacancy();
        vacancy.setPosition(dto.getPosition());
        vacancy.setAddress(dto.getAddress());
        vacancy.setVacancyCategory(dto.getVacancyCategory());


        var profile = clientRepository.findByUserId(clientId)
                .orElseThrow(() -> {
                    log.warn("Client profile not found for userId={}", clientId);
                    return new AbsalyamovRuslanNotFoundException("client with this id is not found!");
                });

        profile.addVacancy(vacancy);
        vacancy.setClientProfile(profile);

        clientRepository.save(profile);

        var clientResponseDto = new AbsalyamovRuslanClientResponseDto();
        clientResponseDto.setCompanyName(profile.getCompanyName());
        clientResponseDto.setCompanyDescription(profile.getCompanyDescription());
        clientResponseDto.setVacancies(profile.getVacancies());

        log.info("Vacancy added for clientId={} totalVacancies={}", clientId, profile.getVacancies().size());
        return clientResponseDto;
    }
}

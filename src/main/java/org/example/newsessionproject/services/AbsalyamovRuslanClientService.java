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
    private final AbsalyamovRuslanClientRepository clientRepository;

    public AbsalyamovRuslanClientService(AbsalyamovRuslanClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public AbsalyamovRuslanClientResponseDto getVacancies(Long userId) {
        var clientProfile = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("client with this id is not found!"));

        var clientResponseDto = new AbsalyamovRuslanClientResponseDto();
        clientResponseDto.setCompanyName(clientProfile.getCompanyName());
        clientResponseDto.setCompanyDescription(clientProfile.getCompanyDescription());
        clientResponseDto.setVacancies(clientProfile.getVacancies());

        return clientResponseDto;
    }

    public AbsalyamovRuslanClientResponseDto addVacancy(Long clientId, AbsalyamovRuslanAddVacancyDto dto) {

        var vacancy = new AbsalyamovRuslanVacancy();
        vacancy.setPosition(dto.getPosition());
        vacancy.setAddress(dto.getAddress());
        vacancy.setVacancyCategory(dto.getVacancyCategory());


        var profile = clientRepository.findByUserId(clientId)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("client with this id is not found!"));

        profile.addVacancy(vacancy);
        vacancy.setClientProfile(profile);

        clientRepository.save(profile);

        var clientResponseDto = new AbsalyamovRuslanClientResponseDto();
        clientResponseDto.setCompanyName(profile.getCompanyName());
        clientResponseDto.setCompanyDescription(profile.getCompanyDescription());
        clientResponseDto.setVacancies(profile.getVacancies());

        return clientResponseDto;
    }
}

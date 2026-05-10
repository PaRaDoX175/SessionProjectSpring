package org.example.newsessionproject.services;

import org.example.newsessionproject.dtos.AbsalyamovRuslanVacancyResponseDto;
import org.example.newsessionproject.enums.AbsalyamovRuslanVacancyCategory;
import org.example.newsessionproject.repositories.AbsalyamovRuslanVacancyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AbsalyamovRuslanVacancyService {
    private final AbsalyamovRuslanVacancyRepository vacancyRepository;

    public AbsalyamovRuslanVacancyService(AbsalyamovRuslanVacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    public List<AbsalyamovRuslanVacancyResponseDto> getVacancies(
            int pageIndex,
            int pageSize,
            String search,
            String sortBy,
            String dir)
    {
        var sort = dir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        var page = PageRequest.of(pageIndex - 1, pageSize, sort);

        var vacancies = vacancyRepository.findByPositionContainingIgnoreCase(search, page).getContent();

        var vacDtos = new ArrayList<AbsalyamovRuslanVacancyResponseDto>();
        for (var v : vacancies) {
            var dto = new AbsalyamovRuslanVacancyResponseDto();
            dto.setPosition(v.getPosition());
            dto.setAddress(v.getPosition());
            dto.setVacancyCategory(v.getVacancyCategory());

            vacDtos.add(dto);
        }

        return vacDtos;
    }
}

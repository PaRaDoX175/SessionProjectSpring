package org.example.newsessionproject.services;

import org.example.newsessionproject.dtos.AbsalyamovRuslanVacancyResponseDto;
import org.example.newsessionproject.enums.AbsalyamovRuslanVacancyCategory;
import org.example.newsessionproject.mappers.AbsalyamovRuslanVacancyMapper;
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
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanVacancyService.class);
    private final AbsalyamovRuslanVacancyRepository vacancyRepository;
    private final AbsalyamovRuslanVacancyMapper vacancyMapper;

    public AbsalyamovRuslanVacancyService(AbsalyamovRuslanVacancyRepository vacancyRepository,
                                          AbsalyamovRuslanVacancyMapper vacancyMapper) {
        this.vacancyRepository = vacancyRepository;
        this.vacancyMapper = vacancyMapper;
    }

    public List<AbsalyamovRuslanVacancyResponseDto> getVacancies(
            int pageIndex,
            int pageSize,
            String search,
            String sortBy,
            String dir)
    {
        log.debug("Fetching vacancies pageIndex={} pageSize={} search='{}' sortBy={} dir={}",
                pageIndex, pageSize, search, sortBy, dir);

        var sort = dir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        var page = PageRequest.of(pageIndex - 1, pageSize, sort);

        var vacancies = vacancyRepository.findByPositionContainingIgnoreCase(search, page).getContent();

        log.info("Vacancies fetched successfully. pageIndex={} resultCount={}", pageIndex, vacancies.size());

        return vacancies.stream().map(vacancyMapper::toResponseDto).toList();
    }
}

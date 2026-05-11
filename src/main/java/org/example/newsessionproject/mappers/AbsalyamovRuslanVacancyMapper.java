package org.example.newsessionproject.mappers;

import org.example.newsessionproject.dtos.AbsalyamovRuslanAddVacancyDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanAddVacancyResponseDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanVacancyResponseDto;
import org.example.newsessionproject.entities.AbsalyamovRuslanVacancy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AbsalyamovRuslanVacancyMapper {
    AbsalyamovRuslanVacancy toEntity(AbsalyamovRuslanAddVacancyDto dto);

    AbsalyamovRuslanVacancyResponseDto toResponseDto(AbsalyamovRuslanVacancy vacancy);
}

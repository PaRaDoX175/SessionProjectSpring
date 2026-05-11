package org.example.newsessionproject.mappers;

import org.example.newsessionproject.dtos.AbsalyamovRuslanAddResumeDto;
import org.example.newsessionproject.entities.AbsalyamovRuslanResume;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AbsalyamovRuslanResumeMapper {
    AbsalyamovRuslanResume toEntity(AbsalyamovRuslanAddResumeDto dto);
}

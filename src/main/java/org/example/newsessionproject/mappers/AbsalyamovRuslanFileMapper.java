package org.example.newsessionproject.mappers;

import org.example.newsessionproject.dtos.AbsalyamovRuslanFileResponseDto;
import org.example.newsessionproject.entities.AbsalyamovRuslanFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AbsalyamovRuslanFileMapper {
    AbsalyamovRuslanFileResponseDto toDto(AbsalyamovRuslanFile file);
}

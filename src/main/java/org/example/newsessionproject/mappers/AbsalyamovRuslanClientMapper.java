package org.example.newsessionproject.mappers;

import org.example.newsessionproject.dtos.AbsalyamovRuslanClientResponseDto;
import org.example.newsessionproject.entities.AbsalyamovRuslanClient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AbsalyamovRuslanClientMapper {
    AbsalyamovRuslanClientResponseDto toDto(AbsalyamovRuslanClient clientProfile);
}

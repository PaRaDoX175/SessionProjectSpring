package org.example.newsessionproject.mappers;

import org.example.newsessionproject.dtos.AbsalyamovRuslanFreelancerResponseDto;
import org.example.newsessionproject.entities.AbsalyamovRuslanFreelancer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AbsalyamovRuslanFreelancerMapper {
    AbsalyamovRuslanFreelancerResponseDto toDto(AbsalyamovRuslanFreelancer freelancerProfile);
}

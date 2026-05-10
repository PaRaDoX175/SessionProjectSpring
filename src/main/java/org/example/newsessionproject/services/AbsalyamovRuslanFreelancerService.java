package org.example.newsessionproject.services;

import org.example.newsessionproject.dtos.AbsalyamovRuslanAddResumeDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanFreelancerResponseDto;
import org.example.newsessionproject.entities.AbsalyamovRuslanResume;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanNotFoundException;
import org.example.newsessionproject.repositories.AbsalyamovRuslanFreelancerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AbsalyamovRuslanFreelancerService {
    private final AbsalyamovRuslanFreelancerRepository freelancerRepository;

    public AbsalyamovRuslanFreelancerService(AbsalyamovRuslanFreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
    }

    public AbsalyamovRuslanFreelancerResponseDto getFreelancer(Long userId) {
        var profile = freelancerRepository.findByUserId(userId)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("Freelancer profile with this id is not found!"));

        var freelancerRespDto = new AbsalyamovRuslanFreelancerResponseDto();
        freelancerRespDto.setName(profile.getName());
        freelancerRespDto.setSurname(profile.getSurname());
        freelancerRespDto.setResume(profile.getResume());

        return freelancerRespDto;
    }

    public AbsalyamovRuslanFreelancerResponseDto addResume(Long userId, AbsalyamovRuslanAddResumeDto dto) {
        var resume = new AbsalyamovRuslanResume();
        resume.setExperience(dto.getExperience());
        resume.setSalary(dto.getSalary());

        var profile = freelancerRepository.findByUserId(userId)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("Freelancer profile with this id is not found!"));

        resume.setFreelancerProfile(profile);
        profile.setResume(resume);

        freelancerRepository.save(profile);

        var freelancerRespDto = new AbsalyamovRuslanFreelancerResponseDto();
        freelancerRespDto.setName(profile.getName());
        freelancerRespDto.setSurname(profile.getSurname());
        freelancerRespDto.setResume(profile.getResume());

        return freelancerRespDto;
    }
}

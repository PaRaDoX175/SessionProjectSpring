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
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanFreelancerService.class);
    private final AbsalyamovRuslanFreelancerRepository freelancerRepository;

    public AbsalyamovRuslanFreelancerService(AbsalyamovRuslanFreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
    }

    public AbsalyamovRuslanFreelancerResponseDto getFreelancer(Long userId) {
        log.debug("Loading freelancer profile for userId={}", userId);
        var profile = freelancerRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Freelancer profile not found for userId={}", userId);
                    return new AbsalyamovRuslanNotFoundException("Freelancer profile with this id is not found!");
                });

        var freelancerRespDto = new AbsalyamovRuslanFreelancerResponseDto();
        freelancerRespDto.setName(profile.getName());
        freelancerRespDto.setSurname(profile.getSurname());
        freelancerRespDto.setResume(profile.getResume());

        log.info("Freelancer profile loaded for userId={}", userId);
        return freelancerRespDto;
    }

    public AbsalyamovRuslanFreelancerResponseDto addResume(Long userId, AbsalyamovRuslanAddResumeDto dto) {
        log.info("Adding resume for freelancer userId={} experience={} salary={}",
                userId, dto.getExperience(), dto.getSalary());
        var resume = new AbsalyamovRuslanResume();
        resume.setExperience(dto.getExperience());
        resume.setSalary(dto.getSalary());

        var profile = freelancerRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Freelancer profile not found for userId={}", userId);
                    return new AbsalyamovRuslanNotFoundException("Freelancer profile with this id is not found!");
                });

        resume.setFreelancerProfile(profile);
        profile.setResume(resume);

        freelancerRepository.save(profile);

        var freelancerRespDto = new AbsalyamovRuslanFreelancerResponseDto();
        freelancerRespDto.setName(profile.getName());
        freelancerRespDto.setSurname(profile.getSurname());
        freelancerRespDto.setResume(profile.getResume());

        log.info("Resume added for freelancer userId={}", userId);
        return freelancerRespDto;
    }
}

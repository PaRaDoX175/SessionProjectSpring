package org.example.newsessionproject.services;

import org.example.newsessionproject.dtos.AbsalyamovRuslanAddResumeDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanFreelancerResponseDto;
import org.example.newsessionproject.entities.AbsalyamovRuslanResume;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanAccessDeniedException;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanNotFoundException;
import org.example.newsessionproject.mappers.AbsalyamovRuslanFreelancerMapper;
import org.example.newsessionproject.mappers.AbsalyamovRuslanResumeMapper;
import org.example.newsessionproject.repositories.AbsalyamovRuslanFreelancerRepository;
import org.example.newsessionproject.repositories.AbsalyamovRuslanResumeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AbsalyamovRuslanFreelancerService {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanFreelancerService.class);
    private final AbsalyamovRuslanFreelancerRepository freelancerRepository;
    private final AbsalyamovRuslanResumeRepository resumeRepository;
    private final AbsalyamovRuslanResumeMapper resumeMapper;
    private final AbsalyamovRuslanFreelancerMapper freelancerMapper;

    public AbsalyamovRuslanFreelancerService(AbsalyamovRuslanFreelancerRepository freelancerRepository,
                                             AbsalyamovRuslanResumeRepository resumeRepository,
                                             AbsalyamovRuslanResumeMapper resumeMapper,
                                             AbsalyamovRuslanFreelancerMapper freelancerMapper) {
        this.freelancerRepository = freelancerRepository;
        this.resumeRepository = resumeRepository;
        this.resumeMapper = resumeMapper;
        this.freelancerMapper = freelancerMapper;
    }

    public AbsalyamovRuslanFreelancerResponseDto getFreelancer(Long userId) {
        log.debug("Loading freelancer profile for userId={}", userId);
        var profile = freelancerRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Freelancer profile not found for userId={}", userId);
                    return new AbsalyamovRuslanNotFoundException("Freelancer profile with this id is not found!");
                });

        log.info("Freelancer profile loaded for userId={}", userId);
        return freelancerMapper.toDto(profile);
    }

    public AbsalyamovRuslanFreelancerResponseDto addResume(Long userId, AbsalyamovRuslanAddResumeDto dto) {
        log.info("Adding resume for freelancer userId={} experience={} salary={}",
                userId, dto.getExperience(), dto.getSalary());

        var resume = resumeMapper.toEntity(dto);

        var profile = freelancerRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Freelancer profile not found for userId={}", userId);
                    return new AbsalyamovRuslanNotFoundException("Freelancer profile with this id is not found!");
                });

        resume.setFreelancerProfile(profile);
        profile.setResume(resume);

        freelancerRepository.save(profile);

        log.info("Resume added for freelancer userId={}", userId);
        return freelancerMapper.toDto(profile);
    }

    public void deleteResume(Long userId, Long resumeId) {
        var resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("Resume is not found"));

        if (!Objects.equals(resume.getFreelancerProfile().getUser().getId(), userId)) {
            throw new AbsalyamovRuslanAccessDeniedException("Only owner can delete this resume!");
        }

        resumeRepository.delete(resume);
    }
}

package org.example.newsessionproject.services;

import org.example.newsessionproject.dtos.AbsalyamovRuslanFileResponseDto;
import org.example.newsessionproject.entities.AbsalyamovRuslanFile;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanAccessDeniedException;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanFileTypeException;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanNotFoundException;
import org.example.newsessionproject.mappers.AbsalyamovRuslanFileMapper;
import org.example.newsessionproject.repositories.AbsalyamovRuslanFileRepository;
import org.example.newsessionproject.repositories.AbsalyamovRuslanFreelancerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AbsalyamovRuslanFileService {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanFileService.class);
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "doc", "docx");

    private final AbsalyamovRuslanFileRepository fileRepository;
    private final AbsalyamovRuslanFreelancerRepository freelancerRepository;
    private final AbsalyamovRuslanEmailService emailService;
    private final AbsalyamovRuslanFileMapper fileMapper;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    public AbsalyamovRuslanFileService(AbsalyamovRuslanFileRepository fileRepository,
                                       AbsalyamovRuslanFreelancerRepository freelancerRepository,
                                       AbsalyamovRuslanEmailService emailService,
                                       AbsalyamovRuslanFileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.freelancerRepository = freelancerRepository;
        this.emailService = emailService;
        this.fileMapper = fileMapper;
    }

    public AbsalyamovRuslanFileResponseDto uploadCv(MultipartFile file, Long userId) {
        log.info("CV upload requested by userId={}", userId);

        var freelancer = freelancerRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Freelancer not found for userId={}", userId);
                    return new AbsalyamovRuslanNotFoundException("Freelancer profile not found");
                });

        String originalName = file.getOriginalFilename();
        String extension = getExtension(originalName);

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            log.warn("Invalid file type={} for userId={}", extension, userId);
            throw new AbsalyamovRuslanFileTypeException(
                    "Unsupported file type. Allowed: PDF, DOC, DOCX");
        }

        try {
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            String storedName = UUID.randomUUID() + "." + extension;
            Path destination = uploadPath.resolve(storedName);

            fileRepository.findByFreelancerId(freelancer.getId()).ifPresent(existing -> {
                log.info("Replacing existing CV for freelancerId={}", freelancer.getId());
                try {
                    Files.deleteIfExists(Paths.get(existing.getFilePath()));
                } catch (IOException e) {
                    log.warn("Could not delete old CV file path={}", existing.getFilePath());
                }
                fileRepository.delete(existing);
            });

            Files.copy(file.getInputStream(), destination);

            var cvFile = new AbsalyamovRuslanFile();
            cvFile.setOriginalFileName(originalName);
            cvFile.setStoredFileName(storedName);
            cvFile.setContentType(file.getContentType());
            cvFile.setFileSize(file.getSize());
            cvFile.setUploadedAt(LocalDateTime.now());
            cvFile.setFilePath(destination.toAbsolutePath().toString());
            cvFile.setFreelancer(freelancer);

            fileRepository.save(cvFile);
            log.info("CV saved successfully for userId={} storedName={}", userId, storedName);

            String email = freelancer.getUser().getEmail();
            sendCvUploadConfirmationAsync(email, originalName);

            return fileMapper.toDto(cvFile);

        } catch (IOException e) {
            log.error("Failed to save CV file for userId={}", userId, e);
            throw new RuntimeException("Failed to save CV file: " + e.getMessage());
        }
    }

    public AbsalyamovRuslanFileResponseDto getUserCv(Long userId) {
        log.debug("Fetching CV metadata for userId={}", userId);
        var freelancer = freelancerRepository.findByUserId(userId)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("Freelancer profile not found"));

        var cvFile = fileRepository.findByFreelancerId(freelancer.getId())
                .orElseThrow(() -> {
                    log.warn("CV not found for freelancerId={}", freelancer.getId());
                    return new AbsalyamovRuslanNotFoundException("CV file not found. Please upload your CV first.");
                });

        return fileMapper.toDto(cvFile);
    }

    public Resource downloadCv(Long freelancerId, Long requestingUserId, List<String> roles) {
        log.info("CV download requested for freelancerId={} by userId={}", freelancerId, requestingUserId);

        var freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("Freelancer not found"));

        boolean isOwner = freelancer.getUser().getId().equals(requestingUserId);
        boolean isClient = roles.contains("ROLE_CLIENT");

        if (!isOwner && !isClient) {
            log.warn("Access denied to CV of freelancerId={} by userId={}", freelancerId, requestingUserId);
            throw new AbsalyamovRuslanAccessDeniedException("You do not have permission to download this CV");
        }

        var cvFile = fileRepository.findByFreelancerId(freelancerId)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("This freelancer has not uploaded a CV yet"));

        try {
            Path filePath = Paths.get(cvFile.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                log.error("CV file not readable path={}", cvFile.getFilePath());
                throw new AbsalyamovRuslanNotFoundException("CV file is no longer available");
            }

            log.info("CV download served for freelancerId={}", freelancerId);
            return resource;

        } catch (MalformedURLException e) {
            log.error("Malformed file path for freelancerId={}", freelancerId, e);
            throw new RuntimeException("Could not read CV file");
        }
    }

    public String getOriginalFileName(Long freelancerId) {
        return fileRepository.findByFreelancerId(freelancerId)
                .map(AbsalyamovRuslanFile::getOriginalFileName)
                .orElse("cv-file");
    }

    public void deleteCv(Long userId) {
        log.info("CV deletion requested by userId={}", userId);
        var freelancer = freelancerRepository.findByUserId(userId)
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("Freelancer profile not found"));

        var cvFile = fileRepository.findByFreelancerId(freelancer.getId())
                .orElseThrow(() -> new AbsalyamovRuslanNotFoundException("No CV found to delete"));

        try {
            Files.deleteIfExists(Paths.get(cvFile.getFilePath()));
        } catch (IOException e) {
            log.warn("Could not delete CV from disk path={}", cvFile.getFilePath());
        }

        fileRepository.delete(cvFile);
        log.info("CV deleted for userId={}", userId);
    }

    @Async
    public void sendCvUploadConfirmationAsync(String email, String fileName) {
        log.info("Sending CV upload confirmation to email={}", email);
        emailService.send(
                email,
                "CV successfully uploaded",
                "Your file \"" + fileName + "\" was successfully uploaded. " +
                        "Now clients can download it."
        );
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}

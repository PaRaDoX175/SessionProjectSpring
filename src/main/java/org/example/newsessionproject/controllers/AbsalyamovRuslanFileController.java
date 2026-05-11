package org.example.newsessionproject.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.example.newsessionproject.dtos.AbsalyamovRuslanFileResponseDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanUserDetails;
import org.example.newsessionproject.services.AbsalyamovRuslanFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class AbsalyamovRuslanFileController {
    private final AbsalyamovRuslanFileService fileService;

    public AbsalyamovRuslanFileController(AbsalyamovRuslanFileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AbsalyamovRuslanFileResponseDto uploadCv(@RequestParam("file") MultipartFile file,
                                                    @AuthenticationPrincipal AbsalyamovRuslanUserDetails userDetails) {

        return fileService.uploadCv(file, userDetails.getId());
    }

    @GetMapping("/cv")
    public AbsalyamovRuslanFileResponseDto getMyCv(@AuthenticationPrincipal AbsalyamovRuslanUserDetails userDetails) {

        return fileService.getUserCv(userDetails.getId());
    }

    @GetMapping("/cv/{freelancerId}/download")
    public ResponseEntity<Resource> downloadCv(@PathVariable Long freelancerId,
                                               @AuthenticationPrincipal AbsalyamovRuslanUserDetails userDetails) {

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Resource resource = fileService.downloadCv(freelancerId, userDetails.getId(), roles);
        String originalFileName = fileService.getOriginalFileName(freelancerId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + originalFileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/cv")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCv(@AuthenticationPrincipal AbsalyamovRuslanUserDetails userDetails) {

        fileService.deleteCv(userDetails.getId());
    }
}

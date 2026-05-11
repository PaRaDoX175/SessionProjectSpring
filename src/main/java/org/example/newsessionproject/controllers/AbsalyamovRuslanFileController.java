package org.example.newsessionproject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Files", description = "File upload, download and management endpoints")
public class AbsalyamovRuslanFileController {
    private final AbsalyamovRuslanFileService fileService;

    public AbsalyamovRuslanFileController(AbsalyamovRuslanFileService fileService) {
        this.fileService = fileService;
    }

    @Operation(
            summary = "Upload CV",
            description = "Uploads a CV file (PDF, DOC, DOCX) for the authenticated freelancer. Replaces existing CV if present."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CV uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Unsupported file type", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Freelancer profile not found", content = @Content)
    })
    @PostMapping(value = "/cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AbsalyamovRuslanFileResponseDto uploadCv(@RequestParam("file") MultipartFile file,
                                                    @AuthenticationPrincipal AbsalyamovRuslanUserDetails userDetails) {

        return fileService.uploadCv(file, userDetails.getId());
    }


    @Operation(
            summary = "Get my CV",
            description = "Returns CV dto."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CV metadata retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "CV not found", content = @Content)
    })
    @GetMapping("/cv")
    public AbsalyamovRuslanFileResponseDto getMyCv(@AuthenticationPrincipal AbsalyamovRuslanUserDetails userDetails) {

        return fileService.getUserCv(userDetails.getId());
    }

    @Operation(
            summary = "Download CV",
            description = "Downloads the CV of a freelancer. Accessible by the owner or clients."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CV file returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "CV not found", content = @Content)
    })
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

    @Operation(
            summary = "Delete CV",
            description = "Deletes the CV of the authenticated freelancer from disk and database."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "CV deleted successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "CV not found", content = @Content)
    })
    @DeleteMapping("/cv")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCv(@AuthenticationPrincipal AbsalyamovRuslanUserDetails userDetails) {

        fileService.deleteCv(userDetails.getId());
    }
}

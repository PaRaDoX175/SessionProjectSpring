package org.example.newsessionproject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.newsessionproject.documents.AbsalyamovRuslanChatMessage;
import org.example.newsessionproject.dtos.AbsalyamovRuslanSendMessageDto;
import org.example.newsessionproject.dtos.AbsalyamovRuslanUserDetails;
import org.example.newsessionproject.services.AbsalyamovRuslanChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat controller", description = "Get and send messages endpoints")
public class AbsalyamovRuslanChatController {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanChatController.class);
    private final AbsalyamovRuslanChatService chatService;

    public AbsalyamovRuslanChatController(AbsalyamovRuslanChatService chatService) {
        this.chatService = chatService;
    }

    @Operation(
            summary = "Get messages by chat ID",
            description = "Returns all messages in the specified chat. Only accessible to chat participants."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Messages retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied to this chat", content = @Content),
            @ApiResponse(responseCode = "404", description = "Chat not found", content = @Content)
    })
    @GetMapping("/messages/{chatId}")
    public List<AbsalyamovRuslanChatMessage> getMessages(@PathVariable String chatId,
                                                         @AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        log.debug("API getMessages called chatId={} userId={}", chatId, user.getId());
        return chatService.getMessages(chatId, user.getId());
    }


    @Operation(
            summary = "Send a message",
            description = "Sends a message to the specified chat on behalf of the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied to this chat", content = @Content)
    })
    @PostMapping("/send")
    public AbsalyamovRuslanChatMessage sendMessage(@RequestBody @Valid AbsalyamovRuslanSendMessageDto dto,
                                   @AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        log.info("API sendMessage called chatId={} senderId={} textLength={}",
                dto.getChatId(), user.getId(), dto.getText() == null ? 0 : dto.getText().length());
        return chatService.sendMessage(dto.getChatId(), user.getId(), dto.getText());
    }
}

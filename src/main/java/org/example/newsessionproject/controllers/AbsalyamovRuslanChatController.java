package org.example.newsessionproject.controllers;

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
public class AbsalyamovRuslanChatController {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanChatController.class);
    private final AbsalyamovRuslanChatService chatService;

    public AbsalyamovRuslanChatController(AbsalyamovRuslanChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/messages/{chatId}")
    public List<AbsalyamovRuslanChatMessage> getMessages(@PathVariable String chatId,
                                                         @AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        log.debug("API getMessages called chatId={} userId={}", chatId, user.getId());
        return chatService.getMessages(chatId, user.getId());
    }

    @PostMapping("/send")
    public AbsalyamovRuslanChatMessage sendMessage(@RequestBody @Valid AbsalyamovRuslanSendMessageDto dto,
                                   @AuthenticationPrincipal AbsalyamovRuslanUserDetails user) {
        log.info("API sendMessage called chatId={} senderId={} textLength={}",
                dto.getChatId(), user.getId(), dto.getText() == null ? 0 : dto.getText().length());
        return chatService.sendMessage(dto.getChatId(), user.getId(), dto.getText());
    }
}

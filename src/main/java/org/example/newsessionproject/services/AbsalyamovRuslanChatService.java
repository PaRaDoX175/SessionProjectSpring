package org.example.newsessionproject.services;

import org.example.newsessionproject.documents.AbsalyamovRuslanChat;
import org.example.newsessionproject.documents.AbsalyamovRuslanChatMessage;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanAccessDeniedException;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanNotFoundException;
import org.example.newsessionproject.repositories.AbsalyamovRuslanChatMessageRepository;
import org.example.newsessionproject.repositories.AbsalyamovRuslanChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbsalyamovRuslanChatService {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanChatService.class);
    private final AbsalyamovRuslanChatRepository chatRepository;
    private final AbsalyamovRuslanChatMessageRepository chatMessageRepository;

    public AbsalyamovRuslanChatService(AbsalyamovRuslanChatRepository chatRepository,
                                       AbsalyamovRuslanChatMessageRepository chatMessageRepository) {
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public AbsalyamovRuslanChat createChat(Long clientId, Long freelancerId, Long vacancyId, Long proposalId) {
        log.info("Creating chat for clientId={} freelancerId={} vacancyId={} proposalId={}",
                clientId, freelancerId, vacancyId, proposalId);
        var chat = new AbsalyamovRuslanChat(clientId, freelancerId, vacancyId, proposalId);
        chatRepository.save(chat);
        log.info("Chat created chatId={}", chat.getId());

        return chat;
    }

    public AbsalyamovRuslanChatMessage sendMessage(String chatId, Long senderId, String text) {
        log.debug("Sending message chatId={} senderId={} textLength={}",
                chatId, senderId, text == null ? 0 : text.length());
        var chat = getChat(chatId, senderId);

        var message = new AbsalyamovRuslanChatMessage(chat.getId(), senderId, text);

        chatMessageRepository.save(message);
        log.info("Chat message sent chatId={} senderId={}", chat.getId(), senderId);

        return message;
    }

    public List<AbsalyamovRuslanChatMessage> getMessages(String chatId, Long senderId) {
        log.debug("Loading chat messages chatId={} requesterId={}", chatId, senderId);
        var chat = getChat(chatId, senderId);

        var messages = chatMessageRepository.findByChatIdOrderBySentAtAsc(chat.getId());
        log.info("Loaded {} messages for chatId={}", messages.size(), chat.getId());
        return messages;
    }

    public List<AbsalyamovRuslanChat> getUsersChat(Long userId) {
        var chats = chatRepository.findByClientIdOrFreelancerId(userId, userId);
        log.info("Loaded {} chats for userId={}", chats.size(), userId);
        return chats;
    }

    private AbsalyamovRuslanChat getChat(String chatId, Long senderId) {
        var chat = chatRepository.findById(chatId)
                .orElseThrow(() -> {
                    log.warn("Chat not found chatId={}", chatId);
                    return new AbsalyamovRuslanNotFoundException("Chat with this id is not found!");
                });

        if (!senderId.equals(chat.getClientId()) && !senderId.equals(chat.getFreelancerId())){
            log.warn("Access denied to chat chatId={} senderId={}", chatId, senderId);
            throw new AbsalyamovRuslanAccessDeniedException("Only chat members can write here!");
        }

        return chat;
    }
}

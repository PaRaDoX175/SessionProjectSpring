package org.example.newsessionproject.services;

import org.example.newsessionproject.documents.AbsalyamovRuslanChat;
import org.example.newsessionproject.documents.AbsalyamovRuslanChatMessage;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanAccessDeniedException;
import org.example.newsessionproject.exceptions.AbsalyamovRuslanNotFoundException;
import org.example.newsessionproject.repositories.AbsalyamovRuslanChatMessageRepository;
import org.example.newsessionproject.repositories.AbsalyamovRuslanChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbsalyamovRuslanChatService {
    private final AbsalyamovRuslanChatRepository chatRepository;
    private final AbsalyamovRuslanChatMessageRepository chatMessageRepository;

    public AbsalyamovRuslanChatService(AbsalyamovRuslanChatRepository chatRepository,
                                       AbsalyamovRuslanChatMessageRepository chatMessageRepository) {
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public AbsalyamovRuslanChat createChat(Long clientId, Long freelancerId, Long vacancyId, Long proposalId) {
        var chat = new AbsalyamovRuslanChat(clientId, freelancerId, vacancyId, proposalId);
        chatRepository.save(chat);

        return chat;
    }

    public AbsalyamovRuslanChatMessage sendMessage(String chatId, Long senderId, String text) {
        var chat = getChat(chatId, senderId);

        var message = new AbsalyamovRuslanChatMessage(chat.getId(), senderId, text);

        chatMessageRepository.save(message);

        return message;
    }

    public List<AbsalyamovRuslanChatMessage> getMessages(String chatId, Long senderId) {
        var chat = getChat(chatId, senderId);

        return chatMessageRepository.findByChatIdOrderBySentAtAsc(chat.getId());
    }

    public List<AbsalyamovRuslanChat> getUsersChat(Long userId) {
        return chatRepository.findByClientIdOrFreelancerId(userId, userId);
    }

    private AbsalyamovRuslanChat getChat(String chatId, Long senderId) {
        var chat = chatRepository.findById(chatId)
                .orElseThrow(() -> {
                    return new AbsalyamovRuslanNotFoundException("Chat with this id is not found!");
                });

        if (!senderId.equals(chat.getClientId()) && !senderId.equals(chat.getFreelancerId())){
            throw new AbsalyamovRuslanAccessDeniedException("Only chat members can write here!");
        }

        return chat;
    }
}

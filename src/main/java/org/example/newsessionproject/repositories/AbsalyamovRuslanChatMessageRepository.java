package org.example.newsessionproject.repositories;

import org.example.newsessionproject.documents.AbsalyamovRuslanChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AbsalyamovRuslanChatMessageRepository extends MongoRepository<AbsalyamovRuslanChatMessage, String> {
    List<AbsalyamovRuslanChatMessage> findByChatIdOrderBySentAtAsc(String chatId);
}

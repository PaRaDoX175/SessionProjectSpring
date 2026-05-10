package org.example.newsessionproject.repositories;

import org.example.newsessionproject.documents.AbsalyamovRuslanChat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AbsalyamovRuslanChatRepository extends MongoRepository<AbsalyamovRuslanChat, Long> {
    List<AbsalyamovRuslanChat> findByClientIdOrFreelancerId(Long clientId, Long freelancerId);
}

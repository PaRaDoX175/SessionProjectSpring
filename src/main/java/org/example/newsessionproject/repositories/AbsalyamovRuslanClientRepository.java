package org.example.newsessionproject.repositories;

import org.example.newsessionproject.entities.AbsalyamovRuslanClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbsalyamovRuslanClientRepository extends JpaRepository<AbsalyamovRuslanClient, Long> {
    Optional<AbsalyamovRuslanClient> findByUserId(Long id);
}

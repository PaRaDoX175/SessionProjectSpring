package org.example.newsessionproject.repositories;

import org.example.newsessionproject.entities.AbsalyamovRuslanUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbsalyamovRuslanUserRepository extends JpaRepository<AbsalyamovRuslanUser, Long> {
    Optional<AbsalyamovRuslanUser> findByEmail(String email);
    boolean existsByEmail(String email);
}

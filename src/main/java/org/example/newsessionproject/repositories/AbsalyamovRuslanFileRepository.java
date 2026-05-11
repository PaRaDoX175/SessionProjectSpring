package org.example.newsessionproject.repositories;

import org.example.newsessionproject.entities.AbsalyamovRuslanFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbsalyamovRuslanFileRepository extends JpaRepository<AbsalyamovRuslanFile, Long> {
    Optional<AbsalyamovRuslanFile> findByFreelancerId(Long freelancerId);
}

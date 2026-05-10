package org.example.newsessionproject.repositories;

import org.example.newsessionproject.entities.AbsalyamovRuslanFreelancer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbsalyamovRuslanFreelancerRepository extends JpaRepository<AbsalyamovRuslanFreelancer, Long> {
    Optional<AbsalyamovRuslanFreelancer> findByUserId(Long userId);
}

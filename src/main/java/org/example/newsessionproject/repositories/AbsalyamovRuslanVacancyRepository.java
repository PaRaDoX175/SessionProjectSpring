package org.example.newsessionproject.repositories;

import org.example.newsessionproject.entities.AbsalyamovRuslanVacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsalyamovRuslanVacancyRepository extends JpaRepository<AbsalyamovRuslanVacancy, Long> {
    Page<AbsalyamovRuslanVacancy> findByPositionContainingIgnoreCase(String position, Pageable pageable);
}

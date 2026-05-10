package org.example.newsessionproject.repositories;

import org.example.newsessionproject.entities.AbsalyamovRuslanVacancyResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbsalyamovRuslanVacancyResponseRepository extends JpaRepository<AbsalyamovRuslanVacancyResponse, Long> {
    List<AbsalyamovRuslanVacancyResponse> findAllByVacancy_ClientProfile_Id(Long id);
    List<AbsalyamovRuslanVacancyResponse> findAllByFreelancerProfile_Id(Long id);
    List<AbsalyamovRuslanVacancyResponse> findAllByVacancy_Id(Long id);
}

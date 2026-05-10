package org.example.newsessionproject.dtos;

import org.example.newsessionproject.entities.AbsalyamovRuslanVacancy;

import java.util.ArrayList;
import java.util.List;

public class AbsalyamovRuslanClientResponseDto {
    private String companyName;
    private String companyDescription;

    private List<AbsalyamovRuslanVacancy> vacancies = new ArrayList<>();

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public List<AbsalyamovRuslanVacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(List<AbsalyamovRuslanVacancy> vacancies) {
        this.vacancies = vacancies;
    }
}

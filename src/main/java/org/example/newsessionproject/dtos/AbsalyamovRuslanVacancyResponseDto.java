package org.example.newsessionproject.dtos;

import org.example.newsessionproject.enums.AbsalyamovRuslanVacancyCategory;

public class AbsalyamovRuslanVacancyResponseDto {
    private String position;
    private String address;
    private AbsalyamovRuslanVacancyCategory vacancyCategory;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AbsalyamovRuslanVacancyCategory getVacancyCategory() {
        return vacancyCategory;
    }

    public void setVacancyCategory(AbsalyamovRuslanVacancyCategory vacancyCategory) {
        this.vacancyCategory = vacancyCategory;
    }
}

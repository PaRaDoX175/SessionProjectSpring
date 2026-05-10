package org.example.newsessionproject.dtos;

import org.example.newsessionproject.enums.AbsalyamovRuslanVacancyCategory;

public class AbsalyamovRuslanAddVacancyDto {
    private String position;
    private AbsalyamovRuslanVacancyCategory vacancyCategory;
    private String address;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public AbsalyamovRuslanVacancyCategory getVacancyCategory() {
        return vacancyCategory;
    }

    public void setVacancyCategory(AbsalyamovRuslanVacancyCategory vacancyCategory) {
        this.vacancyCategory = vacancyCategory;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

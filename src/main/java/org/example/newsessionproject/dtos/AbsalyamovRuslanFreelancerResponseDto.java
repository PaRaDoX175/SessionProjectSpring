package org.example.newsessionproject.dtos;

import org.example.newsessionproject.entities.AbsalyamovRuslanResume;

public class AbsalyamovRuslanFreelancerResponseDto {
    private String name;
    private String surname;
    private AbsalyamovRuslanResume resume;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public AbsalyamovRuslanResume getResume() {
        return resume;
    }

    public void setResume(AbsalyamovRuslanResume resume) {
        this.resume = resume;
    }
}

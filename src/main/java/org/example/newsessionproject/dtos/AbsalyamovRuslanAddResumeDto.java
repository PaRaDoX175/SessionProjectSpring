package org.example.newsessionproject.dtos;

import jakarta.validation.constraints.NotBlank;

public class AbsalyamovRuslanAddResumeDto {
    @NotBlank
    private String experience;
    private int salary;

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}

package org.example.newsessionproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "resumes")
public class AbsalyamovRuslanResume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String experience;
    private int salary;

    @OneToOne
    @JoinColumn(name = "freelancer_profile_id", unique = true, nullable = false)
    @JsonBackReference
    private AbsalyamovRuslanFreelancer freelancerProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public AbsalyamovRuslanFreelancer getFreelancerProfile() {
        return freelancerProfile;
    }

    public void setFreelancerProfile(AbsalyamovRuslanFreelancer freelancerProfile) {
        this.freelancerProfile = freelancerProfile;
    }
}

package org.example.newsessionproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "freelancer")
public class AbsalyamovRuslanFreelancer {

    public AbsalyamovRuslanFreelancer() {

    }
    public AbsalyamovRuslanFreelancer(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonBackReference
    private AbsalyamovRuslanUser user;

    @OneToOne(mappedBy = "freelancerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private AbsalyamovRuslanResume resume;

    @OneToMany(mappedBy = "freelancerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AbsalyamovRuslanVacancyResponse> responses = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AbsalyamovRuslanUser getUser() {
        return user;
    }

    public void setUser(AbsalyamovRuslanUser user) {
        this.user = user;
    }

    public AbsalyamovRuslanResume getResume() {
        return resume;
    }

    public void setResume(AbsalyamovRuslanResume resume) {
        this.resume = resume;
    }

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

    public List<AbsalyamovRuslanVacancyResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<AbsalyamovRuslanVacancyResponse> responses) {
        this.responses = responses;
    }
}

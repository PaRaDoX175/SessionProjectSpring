package org.example.newsessionproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class AbsalyamovRuslanClient {
    public AbsalyamovRuslanClient() {
    }

    public AbsalyamovRuslanClient(String companyName, String companyDescription) {
        this.companyName = companyName;
        this.companyDescription = companyDescription;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String companyDescription;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonBackReference
    private AbsalyamovRuslanUser user;

    @OneToMany(mappedBy = "clientProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AbsalyamovRuslanVacancy> vacancies = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public AbsalyamovRuslanUser getUser() {
        return user;
    }

    public void setUser(AbsalyamovRuslanUser user) {
        this.user = user;
    }

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

    public void addVacancy(AbsalyamovRuslanVacancy vacancy) {
        vacancies.add(vacancy);
    }
}
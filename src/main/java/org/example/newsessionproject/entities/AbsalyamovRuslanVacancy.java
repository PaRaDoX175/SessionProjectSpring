package org.example.newsessionproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.newsessionproject.enums.AbsalyamovRuslanVacancyCategory;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vacancies")
public class AbsalyamovRuslanVacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AbsalyamovRuslanVacancyCategory vacancyCategory;

    @ManyToOne
    @JoinColumn(name = "client_profile_id", nullable = false)
    @JsonBackReference
    private AbsalyamovRuslanClient clientProfile;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AbsalyamovRuslanVacancyResponse> responses = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public AbsalyamovRuslanClient getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(AbsalyamovRuslanClient clientProfile) {
        this.clientProfile = clientProfile;
    }

    public AbsalyamovRuslanVacancyCategory getVacancyCategory() {
        return vacancyCategory;
    }

    public void setVacancyCategory(AbsalyamovRuslanVacancyCategory vacancyCategory) {
        this.vacancyCategory = vacancyCategory;
    }
}

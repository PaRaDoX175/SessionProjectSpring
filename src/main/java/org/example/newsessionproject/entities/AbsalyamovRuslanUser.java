package org.example.newsessionproject.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.newsessionproject.enums.AbsalyamovRuslanRoleName;

@Entity
@Table(name = "users")
public class AbsalyamovRuslanUser {
    public AbsalyamovRuslanUser() {

    }

    public AbsalyamovRuslanUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AbsalyamovRuslanRoleName role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private AbsalyamovRuslanClient clientProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private AbsalyamovRuslanFreelancer freelancerProfile;

    public AbsalyamovRuslanClient getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(AbsalyamovRuslanClient clientProfile) {
        this.clientProfile = clientProfile;
    }

    public AbsalyamovRuslanFreelancer getFreelancerProfile() {
        return freelancerProfile;
    }

    public void setFreelancerProfile(AbsalyamovRuslanFreelancer freelancerProfile) {
        this.freelancerProfile = freelancerProfile;
    }

    public AbsalyamovRuslanRoleName getRole() {
        return role;
    }

    public void setRole(AbsalyamovRuslanRoleName role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

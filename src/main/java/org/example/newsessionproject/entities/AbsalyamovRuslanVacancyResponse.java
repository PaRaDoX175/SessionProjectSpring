package org.example.newsessionproject.entities;

import jakarta.persistence.*;
import org.example.newsessionproject.enums.AbsalyamovRuslanProposalStatus;

@Entity
@Table(name = "vacancy_responses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_freelancer_vacancy",
                        columnNames = {"freelancer_profile_id", "vacancy_id"}
                )
        })
public class AbsalyamovRuslanVacancyResponse {
    public AbsalyamovRuslanVacancyResponse() {}

    public AbsalyamovRuslanVacancyResponse(AbsalyamovRuslanFreelancer freelancerProfile, AbsalyamovRuslanVacancy vacancy) {
        this.freelancerProfile = freelancerProfile;
        this.vacancy = vacancy;
        this.proposalStatus = AbsalyamovRuslanProposalStatus.PENDING;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "freelancer_profile_id", nullable = false)
    private AbsalyamovRuslanFreelancer freelancerProfile;

    @ManyToOne
    @JoinColumn(name = "vacancy_id", nullable = false)
    private AbsalyamovRuslanVacancy vacancy;

    @Enumerated(EnumType.STRING)
    private AbsalyamovRuslanProposalStatus proposalStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AbsalyamovRuslanFreelancer getFreelancerProfile() {
        return freelancerProfile;
    }

    public void setFreelancerProfile(AbsalyamovRuslanFreelancer freelancerProfile) {
        this.freelancerProfile = freelancerProfile;
    }

    public AbsalyamovRuslanVacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(AbsalyamovRuslanVacancy vacancy) {
        this.vacancy = vacancy;
    }

    public AbsalyamovRuslanProposalStatus getProposalStatus() {
        return proposalStatus;
    }

    public void setProposalStatus(AbsalyamovRuslanProposalStatus proposalStatus) {
        this.proposalStatus = proposalStatus;
    }
}

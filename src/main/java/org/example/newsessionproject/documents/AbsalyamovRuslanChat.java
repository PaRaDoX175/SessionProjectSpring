package org.example.newsessionproject.documents;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chats")
public class AbsalyamovRuslanChat {
    public AbsalyamovRuslanChat() {
    }

    public AbsalyamovRuslanChat(Long clientId, Long freelancerId, Long vacancyId, Long proposalId) {
        this.clientId = clientId;
        this.freelancerId = freelancerId;
        this.vacancyId = vacancyId;
        this.proposalId = proposalId;
    }

    @Id
    private String id;
    private Long clientId;
    private Long freelancerId;

    private Long vacancyId;
    private Long proposalId;

    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean isActive = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(Long freelancerId) {
        this.freelancerId = freelancerId;
    }

    public Long getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(Long vacancyId) {
        this.vacancyId = vacancyId;
    }

    public Long getProposalId() {
        return proposalId;
    }

    public void setProposalId(Long proposalId) {
        this.proposalId = proposalId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

package org.example.newsessionproject.dtos;

import org.example.newsessionproject.enums.AbsalyamovRuslanProposalStatus;

public class AbsalyamovRuslanChangeProposalStatusDto {
    private AbsalyamovRuslanProposalStatus status;

    public AbsalyamovRuslanProposalStatus getStatus() {
        return status;
    }

    public void setStatus(AbsalyamovRuslanProposalStatus status) {
        this.status = status;
    }
}

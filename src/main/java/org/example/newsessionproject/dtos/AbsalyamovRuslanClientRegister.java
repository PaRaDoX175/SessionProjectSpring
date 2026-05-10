package org.example.newsessionproject.dtos;

import jakarta.validation.constraints.NotBlank;

public class AbsalyamovRuslanClientRegister extends AbsalyamovRuslanRegister{
    @NotBlank
    private String companyName;
    @NotBlank
    private String companyDescription;

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
}

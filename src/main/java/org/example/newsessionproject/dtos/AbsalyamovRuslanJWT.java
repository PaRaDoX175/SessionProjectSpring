package org.example.newsessionproject.dtos;

public class AbsalyamovRuslanJWT {
    public AbsalyamovRuslanJWT(String token) {
        this.token = token;
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

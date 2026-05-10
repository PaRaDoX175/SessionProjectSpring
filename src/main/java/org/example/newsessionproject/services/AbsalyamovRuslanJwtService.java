package org.example.newsessionproject.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.newsessionproject.entities.AbsalyamovRuslanUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class AbsalyamovRuslanJwtService {
    private final static String SECRET = "cnsjncowekcmajedn1903ie3eufncd3200912nfje2oqmsdk!(#(NN!conadac";
    private final static int EXPIRATION = 3600000;

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(AbsalyamovRuslanUser user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("id", user.getId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignInKey())
                .compact();
    }

    public String getEmailFromToken(String token) {
        return getPayloadFromToken(token).getSubject();
    }

    public Long getIdFromToken(String token) {
        Object id = getPayloadFromToken(token).get("id");

        if (id == null) {
            throw new RuntimeException("JWT token does not contain user id");
        }

        return ((Number)id).longValue();
    }


    public boolean isEmailCorrect(String email, UserDetails userDetails) {
        return email.equals(userDetails.getUsername());
    }

    private Claims getPayloadFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

package com.mercadolibro.util;

import com.mercadolibro.dto.UserDTO;
import com.mercadolibro.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secret;
    private final int expirationInSeconds;

    public JwtUtil(/*@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration}") int expirationInSeconds/*/) {
        this.secret = "secret";
        this.expirationInSeconds = 10000;
    }

    public String generateToken(AppUser appUser) {
        return Jwts.builder()
                .setSubject(appUser.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationInSeconds * 1000L))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateToken(UserDTO userDTO) {
        return Jwts.builder()
                .setSubject(userDTO.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationInSeconds * 1000L))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}

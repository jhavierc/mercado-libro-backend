package com.mercadolibro.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mercadolibro.dto.UserDTO;
import com.mercadolibro.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private final String secret;
    private final int expirationInSeconds;
    private final Gson gson;

    public JwtUtil(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration}") int expirationInSeconds, Gson gson) {
        this.secret = secret;
        this.expirationInSeconds = expirationInSeconds;
        this.gson = gson;
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

    public Map<String, String> getClaimsFromToken(String token) {
        System.out.println("token: " + token);
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        String payload = new String(decoder.decode(chunks[1]));
        return gson.fromJson(payload, new TypeToken<Map<String, String>>(){}.getType());
    }
}

package com.example.inventario.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey signingKey;
    private final long accessTokenExpiration;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-token-expiration-ms}") long accessTokenExpiration) {
        // Convierte el texto secreto de application.properties en una llave criptográfica
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
    }

    // Construye el Access Token JWT
    public String generateAccessToken(UserDetails user) {
        Date now = new Date();
        return Jwts.builder()
                .subject(user.getUsername()) // Guarda el nombre de usuario dentro del token
                .issuedAt(now) // Fecha de emisión
                .expiration(new Date(now.getTime() + accessTokenExpiration)) // Fecha límite (15 min)
                .signWith(signingKey) // Firma el token para evitar manipulaciones
                .compact();
    }

    // Lee el token y saca el nombre del usuario
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // Valida que el token pertenezca al usuario y no esté vencido
    public boolean isValid(String token, UserDetails user) {
        Claims claims = parseClaims(token);
        return claims.getSubject().equals(user.getUsername())
                && claims.getExpiration().after(new Date());
    }

    // Método interno para desencriptar y leer el cuerpo (claims) del token
    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(signingKey).build()
                .parseSignedClaims(token).getPayload();
    }
}
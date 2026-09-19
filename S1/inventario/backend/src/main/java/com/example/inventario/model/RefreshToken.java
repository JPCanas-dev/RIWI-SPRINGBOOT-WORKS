package com.example.inventario.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;

@Entity
public class RefreshToken {
    @Id
    private String token; // El token será la llave primaria
    private String username; // A qué usuario le pertenece
    private Instant expiresAt; // Cuándo caduca (7 días)
    private boolean revoked; // Si está revocado (ej. si el usuario cerró sesión)

    // Constructor vacío obligatorio para JPA
    public RefreshToken() {}

    // Constructor para crear nuevos tokens
    public RefreshToken(String token, String username, Instant expiresAt) {
        this.token = token;
        this.username = username;
        this.expiresAt = expiresAt;
    }

    // Getters y Setters
    public String getToken() { return token; }
    public String getUsername() { return username; }
    public Instant getExpiresAt() { return expiresAt; }
    public boolean isRevoked() { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }
}

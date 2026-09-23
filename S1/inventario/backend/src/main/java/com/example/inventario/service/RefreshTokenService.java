package com.example.inventario.service;

import com.example.inventario.model.RefreshToken;
import com.example.inventario.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository repository;
    private final long refreshTokenExpiration;

    public RefreshTokenService(RefreshTokenRepository repository,
                               @Value("${security.jwt.refresh-token-expiration-ms}") long refreshTokenExpiration) {
        this.repository = repository;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // Crea y guarda un nuevo Refresh Token en la BD
    public RefreshToken create(String username) {
        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(), // Genera un código aleatorio único
                username,
                Instant.now().plusMillis(refreshTokenExpiration) // Fecha actual + 7 días
        );
        return repository.save(refreshToken);
    }

    // Verifica si el token existe, no ha sido revocado y no ha expirado
    public RefreshToken validate(String token) {
        RefreshToken refreshToken = repository.findById(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (refreshToken.isRevoked() || refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Token inválido o expirado");
        }
        return refreshToken;
    }

    // Marca un token como revocado para que no se pueda volver a usar
    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        repository.save(token);
    }
}
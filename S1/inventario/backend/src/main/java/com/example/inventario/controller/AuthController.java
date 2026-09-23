package com.example.inventario.controller;

// --- Estos son los imports de tus propias carpetas que faltaban ---
import com.example.inventario.dto.AuthResponse;
import com.example.inventario.dto.LoginRequest;
import com.example.inventario.dto.RefreshRequest;
import com.example.inventario.model.RefreshToken;
import com.example.inventario.security.JwtService;
import com.example.inventario.service.RefreshTokenService;

// --- Imports de Spring Boot y Java ---
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final long accessTokenExpiration;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
                          RefreshTokenService refreshTokenService, UserDetailsService userDetailsService,
                          @Value("${security.jwt.access-token-expiration-ms}") long accessTokenExpiration) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userDetailsService = userDetailsService;
        this.accessTokenExpiration = accessTokenExpiration;
    }

    // Ruta 1: Iniciar Sesión (POST http://localhost:8080/auth/login)
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        // Valida que el usuario y clave sean "admin" y "admin123" usando Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        // Si todo está bien, genera los tokens
        return issueTokens(request.username());
    }

    // Ruta 2: Refrescar Token (POST http://localhost:8080/auth/refresh)
    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshRequest request) {
        // Verifica que el viejo refresh token sea válido en la BD
        RefreshToken current = refreshTokenService.validate(request.refreshToken());

        // Revoca (cancela) el viejo para que no se re-utilice (Seguridad)
        refreshTokenService.revoke(current);

        // Genera un par de tokens completamente nuevos
        return issueTokens(current.getUsername());
    }

    // Método de apoyo para crear los tokens y devolver el AuthResponse
    private AuthResponse issueTokens(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String accessToken = jwtService.generateAccessToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.create(username);

        return new AuthResponse(accessToken, refreshToken.getToken(), accessTokenExpiration / 1000);
    }
}
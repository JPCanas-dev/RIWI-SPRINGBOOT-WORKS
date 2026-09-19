package com.example.inventario.dto;

// Lo que le respondemos al cliente cuando el login es exitoso
public record AuthResponse(String accessToken, String refreshToken, long expiresIn)  {
}

package com.example.inventario.dto;

// Lo que envía el cliente (Postman/Angular) para iniciar sesión
public record LoginRequest(String username, String password) {
}

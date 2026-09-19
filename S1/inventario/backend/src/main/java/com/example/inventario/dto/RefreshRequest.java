package com.example.inventario.dto;

// Lo que envía el cliente para pedir un nuevo Access Token cuando el anterior vence
public record RefreshRequest(String refreshToken) {

}

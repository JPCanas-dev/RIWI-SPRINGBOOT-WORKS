package com.example.inventario.model;

import jakarta.persistence.*;

@Entity // // Le dice a Spring que esta clase será una tabla en la base de datos
public class Categoria {

    @Id // Define que este campo es la Llave Primaria (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Hace que el ID sea autoincrementable
    private Long id;

    private String nombre;

    // 1. Constructor vacío (OBLIGATORIO para que JPA funcione)
    public Categoria() {
    }

    // 2. Constructor con todos los parámetros (Opcional, pero muy útil)
    public Categoria(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
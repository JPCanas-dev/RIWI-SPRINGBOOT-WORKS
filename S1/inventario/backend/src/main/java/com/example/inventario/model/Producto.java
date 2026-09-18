package com.example.inventario.model;

import jakarta.persistence.*;

@Entity
public class Producto {

    @Id // Define que este campo es la Llave Primaria (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Double precio;

    // Relación: Muchos productos pueden pertenecer a una sola categoría
    @ManyToOne
    @JoinColumn(name = "categoria_id") // Esto crea la columna "categoria_id" en la tabla producto
    private Categoria categoria;

    // 1. Constructor vacío (OBLIGATORIO)
    public Producto() {
    }

    // 2. Constructor con parámetros
    public Producto(Long id, String nombre, Double precio, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    // Getters and setters
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

}

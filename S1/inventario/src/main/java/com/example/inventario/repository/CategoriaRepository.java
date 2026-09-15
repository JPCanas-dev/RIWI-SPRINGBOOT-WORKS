package com.example.inventario.repository;

// --- IMPORTACIONES ---

import com.example.inventario.model.Categoria;

// 1. Traemos la clase Producto para que el repositorio sepa que administrará productos.
import com.example.inventario.model.Producto;

// 2. Traemos JpaRepository para heredar todos los métodos CRUD automáticos.
import org.springframework.data.jpa.repository.JpaRepository;

// 3. Traemos la anotación @Repository para que Spring la reconozca.
import org.springframework.stereotype.Repository;

// Cambiamos "class" por "interface" y le agregamos el "extends JpaRepository"
@Repository // Indica que esta interfaz maneja la persistencia de los productos
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Hereda los métodos CRUD automáticamente para la tabla Producto.
}
package com.example.inventario.repository;

// 1. Traemos la clase Producto para que este repositorio sepa qué entidad va a administrar
import com.example.inventario.model.Producto;

// 2. Traemos JpaRepository para heredar los métodos CRUD automáticos
import org.springframework.data.jpa.repository.JpaRepository;

// 3. Traemos la anotación @Repository para que Spring reconozca esta interfaz
import org.springframework.stereotype.Repository;

@Repository // Le avisa a Spring que esta interfaz maneja los datos de los productos
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Al igual que con categoría, hereda todos los métodos para guardar, listar y borrar productos.

    // NUEVO: Método para saber si hay productos usando una categoría
    boolean existsByCategoriaId(Long categoriaId);

}
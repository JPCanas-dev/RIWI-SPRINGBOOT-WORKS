package com.example.inventario.service;

import com.example.inventario.model.Producto;
import com.example.inventario.repository.CategoriaRepository; // Importamos para validar la categoría
import com.example.inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository; // Lo inyectamos para validar si la categoría existe

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // 1. Listar todos
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // 2. Buscar por ID
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    // 3. Crear producto con VALIDACIONES EXHAUSTIVAS
    public Producto crearProducto(Producto producto) {
        // Validar nombre
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }

        // Validar precio
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
        }

        // Validar que venga con una categoría
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new IllegalArgumentException("El producto debe tener una categoría asignada.");
        }

        // Validar que la categoría exista realmente en la Base de Datos
        if (!categoriaRepository.existsById(producto.getCategoria().getId())) {
            throw new IllegalArgumentException("La categoría asignada (ID: " + producto.getCategoria().getId() + ") no existe.");
        }

        return productoRepository.save(producto);
    }

    // 4. Eliminar con validación previa
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar: El producto con ID " + id + " no existe.");
        }
        productoRepository.deleteById(id);
    }
}
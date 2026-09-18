package com.example.inventario.service;

import com.example.inventario.model.Producto;
import com.example.inventario.repository.CategoriaRepository; // Importamos para validar la categoría
import com.example.inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    // Búsqueda simple: Si no lo encuentra, lanza el error de una vez
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El producto con ID " + id + " no existe."));
    }

    /*
    Opción 2: uso de Optional, solo busca y dice "lo encontré" o "no lo encontré". Y el controller
    es el que maneja la respuesta. Todo esto sin usar try-catch y throw new en controller o service.

    Hay que import java.util.Optional;
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }
     */

    // 3. Crear producto con validaciones exhaustivas
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

    // 4. Actualizar producto
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        // Validamos que el producto exista primero
        Producto productoExistente = obtenerPorId(id);

        // Validaciones de los nuevos datos
        if (productoActualizado.getNombre() == null || productoActualizado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (productoActualizado.getPrecio() == null || productoActualizado.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
        }
        if (productoActualizado.getCategoria() == null || productoActualizado.getCategoria().getId() == null) {
            throw new IllegalArgumentException("El producto debe tener una categoría asignada.");
        }
        if (!categoriaRepository.existsById(productoActualizado.getCategoria().getId())) {
            throw new IllegalArgumentException("La categoría asignada no existe.");
        }

        // Actualizamos los campos
        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setCategoria(productoActualizado.getCategoria());

        return productoRepository.save(productoExistente);
    }

    // 4. Eliminar con validación previa
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar: El producto con ID " + id + " no existe.");
        }
        productoRepository.deleteById(id);
    }
}
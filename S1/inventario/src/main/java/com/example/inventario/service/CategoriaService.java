package com.example.inventario.service;

import com.example.inventario.model.Categoria;
import com.example.inventario.repository.CategoriaRepository;
// Para no borrar una categoria con producto asignado, importamos:
import com.example.inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository; // LO AGREGAMOS AQUÍ

    // ACTUALIZAMOS EL CONSTRUCTOR PARA INYECTAR AMBOS
    public CategoriaService(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    // 1. Listar todas
    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll();
    }

    // 2. Buscar por ID
    // Buscar por ID (Si no existe, lanza el error de una vez)
    public Categoria obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La categoría con ID " + id + " no existe."));
    }

    /*
    Opción 2: uso de Optional, solo busca y dice "lo encontré" o "no lo encontré". Y el controller
    es el que maneja la respuesta. Todo esto sin usar try-catch y throw new en controller o service.

    Hay que import java.util.Optional;
    public Optional<Categoria> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }
     */

    // 3. Guardar con VALIDACIONES
    public Categoria crearCategoria(Categoria categoria) {
        // Validamos que el nombre no sea nulo ni esté vacío
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
        }
        return categoriaRepository.save(categoria);
    }

    // 4. Actualizar categoría
    public Categoria actualizarCategoria(Long id, Categoria categoriaActualizada) {
        // Primero verificamos que la categoría exista (si no, lanza el error de una vez)
        Categoria categoriaExistente = obtenerCategoriaPorId(id);

        // Validamos que el nuevo nombre no venga vacío
        if (categoriaActualizada.getNombre() == null || categoriaActualizada.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        // Modificamos el nombre y guardamos (Spring save() actualiza si el ID ya existe)
        categoriaExistente.setNombre(categoriaActualizada.getNombre());
        return categoriaRepository.save(categoriaExistente);
    }

    // 5. Eliminar con validación previa
    public void eliminarCategoria(Long id) {
        // Validación 1: ¿La categoría existe?
        if (!categoriaRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar: La categoría con ID " + id + " no existe.");
        }

        // Validación 2: ¿La categoría tiene productos asignados?
        if (productoRepository.existsByCategoriaId(id)) {
            // Usamos IllegalStateException para indicar que el estado actual no permite la acción
            throw new IllegalStateException("No se puede eliminar: Hay productos que pertenecen a esta categoría.");
        }

        // Si pasa las dos validaciones, la borramos tranquilos
        categoriaRepository.deleteById(id);
    }
}
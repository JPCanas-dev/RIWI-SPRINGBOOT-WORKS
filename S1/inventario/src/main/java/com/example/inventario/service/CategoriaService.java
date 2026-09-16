package com.example.inventario.service;

import com.example.inventario.model.Categoria;
import com.example.inventario.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // 1. Listar todas
    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll();
    }

    // 2. Buscar por ID
    public Optional<Categoria> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    // 3. Guardar con VALIDACIONES
    public Categoria crearCategoria(Categoria categoria) {
        // Validamos que el nombre no sea nulo ni esté vacío
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
        }
        return categoriaRepository.save(categoria);
    }

    // 4. Eliminar con validación previa
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar: La categoría con ID " + id + " no existe.");
        }
        categoriaRepository.deleteById(id);
    }
}
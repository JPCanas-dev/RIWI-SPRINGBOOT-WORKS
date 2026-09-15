package com.example.inventario.controller;

// --- IMPORTACIONES ---
import com.example.inventario.model.Categoria;
import com.example.inventario.repository.CategoriaRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Le dice a Spring: "Esta clase es un controlador web que responderá con datos (como JSON)"
@RequestMapping("/api/categorias") // Define la ruta base para todos los endpoints de este controlador
public class CategoriaController {

    // Inyectamos el repositorio para poder usar sus métodos CRUD
    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // 1. ENDPOINT GET: Listar todas las categorías (Ej: GET http://localhost:8080/api/categorias)
    @GetMapping
    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll(); // Usa el metodo de JpaRepository
    }

    // 2. ENDPOINT POST: Crear una nueva categoría (Ej: POST http://localhost:8080/api/categorias)
    @PostMapping
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        // @RequestBody le dice a Spring: "Toma el JSON que viene en el cuerpo de la petición y conviértelo en un objeto Categoria"
        return categoriaRepository.save(categoria);
    }

    // 3. ENDPOINT DELETE: Eliminar una categoría por su ID (Ej: DELETE http://localhost:8080/api/categorias/1)
    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        // @PathVariable atrapa el {id} que mandes en la URL
        categoriaRepository.deleteById(id);
    }
}

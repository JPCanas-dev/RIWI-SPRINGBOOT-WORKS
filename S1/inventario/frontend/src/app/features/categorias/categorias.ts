import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Para directivas como *ngIf
import { FormsModule } from '@angular/forms'; // Para [(ngModel)] en el formulario
import { Router } from '@angular/router';

// CORRECCIÓN 1: Importamos el servicio quitando el punto extra al final de la ruta
import { CategoriaService } from '../../core/services/categoria.service';
import { Categoria } from '../../shared/models/producto';

@Component({
  selector: 'app-categorias',
  standalone: true,
  imports: [CommonModule, FormsModule], // Activamos herramientas de Angular
  templateUrl: './categorias.html' // O el nombre que tenga tu HTML
})
export class CategoriasComponent implements OnInit {
  // Inyectamos el enrutador y el servicio de categorías
  private router = inject(Router);
  private categoriaService = inject(CategoriaService);

  // Variables para los datos
  categorias: Categoria[] = [];
  errorMessage = '';
  modoEdicion = false;

  // Objeto por defecto para crear o editar
  nuevaCategoria: Categoria = {
    id: 0,
    nombre: ''
  };

  // Al cargar la pantalla, traemos las categorías
  ngOnInit() {
    this.cargarCategorias();
  }

  // GET: Obtener todas las categorías desde Spring Boot
  cargarCategorias() {
    this.categoriaService.obtenerTodas().subscribe({
      // CORRECCIÓN 2: Declaramos explicitamente el tipo (data: Categoria[])
      next: (data: Categoria[]) => this.categorias = data,
      // CORRECCIÓN 3: Declaramos explicitamente el tipo (err: any)
      error: (err: any) => {
        this.errorMessage = 'Error al cargar las categorías.';
        console.error(err);
      }
    });
  }

  // POST / PUT: Crear o actualizar
 guardar() {
    if (this.modoEdicion && this.nuevaCategoria.id) {
      // Si estamos editando, usamos PUT y SI enviamos el ID en la URL y el cuerpo
      this.categoriaService.actualizarCategoria(this.nuevaCategoria.id, this.nuevaCategoria).subscribe({
        next: () => {
          this.cargarCategorias();
          this.cancelarEdicion();
        },
        error: (err: any) => {
          this.errorMessage = 'Error al actualizar la categoría. ' + (err.error || '');
          console.error(err);
        }
      });
    } else {
      // Lógica de CREAR (POST)
      // NUEVO: Creamos un objeto temporal SOLO con el nombre
      const categoriaParaCrear = { nombre: this.nuevaCategoria.nombre }; 
      
      // Enviamos ese objeto temporal, obligando a TypeScript a aceptarlo como Categoria
      this.categoriaService.crearCategoria(categoriaParaCrear as Categoria).subscribe({
        next: () => {
          this.cargarCategorias();
          this.cancelarEdicion();
        },
        error: (err: any) => {
          this.errorMessage = 'Error al crear la categoría. ' + (err.error || '');
          console.error(err);
        }
      });
    }
  }

  // Preparar el formulario para editar
  editar(categoria: Categoria) {
    this.modoEdicion = true;
    this.errorMessage = '';
    this.nuevaCategoria = { ...categoria }; // Clonamos los datos
  }

  // Limpiar el formulario
  cancelarEdicion() {
    this.modoEdicion = false;
    this.nuevaCategoria = { id: 0, nombre: '' };
    this.errorMessage = '';
  }

  // DELETE: Eliminar categoría
  eliminar(id: number) {
    if (confirm('¿Estás seguro de eliminar esta categoría?')) {
      this.categoriaService.eliminarCategoria(id).subscribe({
        next: () => {
          this.cargarCategorias();
          this.errorMessage = '';
        },
        // CORRECCIÓN 3: Explicitamos (err: any)
        error: (err: any) => {
          // Si Spring Boot responde con conflicto (409) o error, lo mostramos
          this.errorMessage = err.error || 'Error desconocido al intentar eliminar.';
          console.error(err);
        }
      });
    }
  }

  // Volver al Dashboard de productos
  volver() {
    this.router.navigate(['/dashboard']);
  }
}
import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Necesario para directivas básicas como *ngIf
import { FormsModule } from '@angular/forms'; // Necesario para que funcione [(ngModel)] en el HTML
import { Router } from '@angular/router';
// Importamos nuestros servicios y los modelos
import { ProductoService } from '../../core/services/producto.service';
import { CategoriaService } from '../../core/services/categoria.service'; // Inyectamos el servicio de categorías
import { Producto, Categoria } from '../../shared/models/producto'; // NUEVO: Importamos también la interfaz Categoria

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule], // Activamos FormsModule y CommonModule aquí
  templateUrl: './dashboard.html'
})
export class Dashboard implements OnInit {
  // Inyectamos el enrutador para redirecciones y los servicios para llamadas HTTP
  private router = inject(Router);
  private productoService = inject(ProductoService);
  private categoriaService = inject(CategoriaService); // NUEVO: Inyectamos el servicio de categorías

  // Variables para guardar los datos que lleguen del backend
  productos: Producto[] = [];
  categorias: Categoria[] = []; // NUEVO: Arreglo vacío que se llenará con datos reales de Spring Boot
  errorMessage = '';

  // Bandera para saber si estamos creando (false) o editando (true)
  modoEdicion = false;

  // Variables para nuestro formulario (valores por defecto)
  nuevoProducto: Producto = {
    nombre: '',
    precio: 0,
    categoria: { id: 1 } // Por defecto arrancará con ID 1, luego se actualiza dinámicamente
  };

  // ngOnInit se ejecuta automáticamente apenas carga la pantalla
  ngOnInit() {
    this.cargarCategorias(); // NUEVO: Primero cargamos las categorías para el select
    this.cargarProductos(); // Luego cargamos la tabla inicial de productos
  }

  // NUEVO: Función para traer categorías reales de la Base de Datos
  cargarCategorias() {
    this.categoriaService.obtenerTodas().subscribe({
      next: (data: Categoria[]) => {
        this.categorias = data; // Guardamos las categorías reales
        // Si hay categorías, asignamos la primera por defecto al formulario para evitar errores visuales
        if (this.categorias.length > 0) {
          this.nuevoProducto.categoria.id = this.categorias[0].id;
        }
      },
      error: (err: any) => console.error('Error cargando categorías:', err)
    });
  }

  // Función para llamar al backend mediante método GET
  cargarProductos() {
    this.productoService.obtenerTodos().subscribe({
      next: (data: Producto[]) => {
        this.productos = data; // Si hay éxito, guardamos la lista de productos
      },
      error: (err: any) => {
        this.errorMessage = 'Hubo un problema al cargar el inventario.';
        console.error('Error cargando productos:', err);
      }
    });
  }

  // Ahora esta función decide si llama a CREAR o a ACTUALIZAR dependiendo del modo
  guardar() {
    if (this.modoEdicion && this.nuevoProducto.id) {
      // Lógica de ACTUALIZAR (PUT)
      this.productoService.actualizarProducto(this.nuevoProducto.id, this.nuevoProducto).subscribe({
        next: () => {
          this.cargarProductos(); // Refresca la tabla
          this.cancelarEdicion(); // Limpia el formulario y vuelve a modo creación
        },
        error: (err: any) => {
          this.errorMessage = 'Error al actualizar el producto.';
          console.error('Error al actualizar:', err);
        }
      });
    } else {
      // Lógica de CREAR (POST)
      this.productoService.crearProducto(this.nuevoProducto).subscribe({
        next: () => {
          this.cargarProductos(); // Refresca la tabla
          this.cancelarEdicion(); // Limpia el formulario
        },
        error: (err: any) => {
          this.errorMessage = 'Error al crear el producto. Revisa los datos.';
          console.error('Error al crear:', err);
        }
      });
    }
  }

  // Función para poner los datos de la fila en el formulario
  editar(producto: Producto) {
    this.modoEdicion = true;
    this.errorMessage = '';
    // Clonamos el objeto para que si el usuario edita y luego cancela, no se modifique la tabla directamente
    this.nuevoProducto = { ...producto, categoria: { ...producto.categoria } };
  }

  // Función para cancelar la edición y limpiar el formulario
  cancelarEdicion() {
    this.modoEdicion = false;
    // NUEVO: Volvemos a asignar la primera categoría real por defecto si existe
    const defaultCatId = this.categorias.length > 0 ? this.categorias[0].id : 1;
    this.nuevoProducto = { nombre: '', precio: 0, categoria: { id: defaultCatId } };
    this.errorMessage = '';
  }

  // Función para Eliminar (DELETE)
  eliminar(id: number | undefined) {
    if (!id) return; // Si por alguna razón no hay ID, cancelamos
    
    // Mostramos alerta nativa del navegador para confirmación
    if (confirm('¿Estás seguro de que deseas eliminar este producto?')) {
      this.productoService.eliminarProducto(id).subscribe({
        next: () => {
          this.cargarProductos(); // Refrescamos la tabla tras borrar exitosamente
          this.errorMessage = '';
        },
        error: (err: any) => {
          this.errorMessage = 'Error al eliminar. Tal vez no tienes permisos o está restringido (Foreign Key).';
          console.error('Error al eliminar:', err);
        }
      });
    }
  }

  // Función para navegar a la pantalla de Categorías
  irACategorias() {
    this.router.navigate(['/categorias']);
  }

  // Cierre de sesión: limpia tokens y redirige al login
  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.router.navigate(['/login']);
  }
}
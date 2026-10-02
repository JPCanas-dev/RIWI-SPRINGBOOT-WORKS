import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto } from '../../shared/models/producto'; // Ruta de tu modelo

@Injectable({
  providedIn: 'root' // Disponible en toda la aplicación
})
export class ProductoService {
  // Inyectamos la herramienta para hacer peticiones HTTP
  private http = inject(HttpClient);
  
  // La URL exacta de tu controlador en Spring Boot
  private apiUrl = 'http://localhost:8080/api/productos';

  // Método GET: Devuelve un arreglo de productos desde Java
  obtenerTodos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.apiUrl);
  }

  // Método POST para enviar un producto a Spring Boot y guardarlo
  crearProducto(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, producto);
  }

  // Método DELETE pasándole el ID en la URL (ej: /api/productos/5)
  eliminarProducto(id: number): Observable<any> {
    // IMPORTANTE: Usamos responseType: 'text' porque tu backend (ProductoController) 
    // devuelve un String ("Producto eliminado con éxito.") y no un JSON al eliminar.
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' }); 
  }

  // NUEVO: Método PUT para actualizar un producto existente pasándole el ID y los nuevos datos
  actualizarProducto(id: number, producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}`, producto);
  }
}
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
// Reutilizamos la interfaz Categoria que ya habíamos definido dentro de producto.ts
import { Categoria } from '../../shared/models/producto'; 

@Injectable({
  providedIn: 'root' // Disponible en toda la aplicación sin necesidad de registrarlo en módulos
})
export class CategoriaService {
  // Inyectamos la herramienta de Angular para hacer peticiones HTTP
  private http = inject(HttpClient);
  
  // La URL exacta que configuraste en tu CategoriaController de Spring Boot
  private apiUrl = 'http://localhost:8080/api/categorias';

  // Método GET: Devuelve un arreglo de categorías desde la base de datos
  obtenerTodas(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.apiUrl);
  }

  // Método GET por ID: Consume el endpoint /api/categorias/{id}
  obtenerPorId(id: number): Observable<Categoria> {
    return this.http.get<Categoria>(`${this.apiUrl}/${id}`);
  }

  // Método POST: Envía el JSON de una nueva categoría para guardarla
  crearCategoria(categoria: Categoria): Observable<Categoria> {
    return this.http.post<Categoria>(this.apiUrl, categoria);
  }

  // Método PUT: Envía el ID en la URL y los nuevos datos en el cuerpo (body) para actualizar
  actualizarCategoria(id: number, categoria: Categoria): Observable<Categoria> {
    return this.http.put<Categoria>(`${this.apiUrl}/${id}`, categoria);
  }

  // Método DELETE: Pasa el ID en la URL para borrar la categoría
  eliminarCategoria(id: number): Observable<any> {
    // IMPORTANTE: Usamos responseType: 'text' porque tu backend 
    // devuelve un String simple ("Categoría eliminada con éxito.") en lugar de un objeto JSON.
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' }); 
  }
}
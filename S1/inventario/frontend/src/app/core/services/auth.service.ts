import { Injectable, inject } from '@angular/core';
// Importamos la herramienta para hacer peticiones HTTP (GET, POST, etc.)
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../shared/models/user';
import { Token } from '../../shared/models/token';

// @Injectable hace que este servicio esté disponible en toda la aplicación
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Ruta base de autenticación en tu Spring Boot
  private apiUrl = 'http://localhost:8080/auth'; 

  // Inyección de dependencias moderna en Angular
  private http = inject(HttpClient);

  constructor() { }

  // Envía las credenciales y espera de vuelta el objeto con accessToken, refreshToken y expiresIn
  login(credentials: User): Observable<Token> {
    return this.http.post<Token>(`${this.apiUrl}/login`, credentials);
  }
}
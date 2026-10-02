import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
// Importamos las herramientas para formularios reactivos
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

// IMPORTANTE: Verifica que esta ruta apunte a tu servicio. 
// Si lo dejaste como auth.ts, cámbialo aquí a '../core/services/auth'
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  // Activamos los módulos necesarios para esta vista
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login {
  // 1. Inyectamos nuestro servicio de autenticación y el enrutador
  private authService = inject(AuthService);
  private router = inject(Router);

  // 2. Creamos el formulario definiendo los campos que espera tu Spring Boot
  loginForm = new FormGroup({
    username: new FormControl('', Validators.required), // Cambia 'username' por 'email' si tu backend usa correo
    password: new FormControl('', Validators.required)
  });

  // Mensaje para mostrar si la contraseña es incorrecta
  errorMessage = '';

  // 3. Función que se dispara al hacer clic en el botón del formulario
  onSubmit() {
    if (this.loginForm.valid) {
      // Tomamos los datos escritos
      const credentials = {
        username: this.loginForm.value.username!,
        password: this.loginForm.value.password!
      };

      // 4. Llamamos a Spring Boot a través del servicio
      this.authService.login(credentials).subscribe({
        next: (response) => {
          // Si es exitoso, guardamos el token en el navegador
          localStorage.setItem('accessToken', response.accessToken);
          
          if (response.refreshToken) {
            localStorage.setItem('refreshToken', response.refreshToken);
          }
          
          // El Guardián ahora nos dejará pasar al dashboard
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          // Si Spring Boot rechaza las credenciales
          this.errorMessage = 'Credenciales incorrectas. Intenta nuevamente.';
          console.error('Error de login:', err);
        }
      });
    }
  }
}
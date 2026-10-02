import { Routes } from '@angular/router';

// 1. Importamos tus componentes
import { Login } from './features/login/login';
import { Dashboard } from './features/dashboard/dashboard';
// Importamos el componente de categorías
import { CategoriasComponent } from './features/categorias/categorias';

// 2. Importamos a nuestro guardián de seguridad
import { authGuard } from './core/guards/auth-guard'; 

// Las rutas siempre son un arreglo (array) y SE EVALÚAN DE ARRIBA HACIA ABAJO
export const routes: Routes = [
  // Ruta por defecto: si entras a la raíz, te manda al login
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  
  // Ruta pública: cualquiera puede ver la pantalla de Login
  { path: 'login', component: Login },
  
  // Ruta PRIVADA 1: Dashboard de productos
  { 
    path: 'dashboard', 
    component: Dashboard,
    canActivate: [authGuard] // El guardián protege esta ruta
  },

  // Ruta PRIVADA 2: Administración de Categorías
  // CORRECCIÓN: La subimos antes de la ruta comodín y le agregamos el guardián
  { 
    path: 'categorias', 
    component: CategoriasComponent,
    canActivate: [authGuard] // También debemos proteger esta pantalla
  },

  // Ruta comodín (**): ¡SIEMPRE DEBE IR AL FINAL! 
  // Si escriben una URL rara (o una que no existe), los devolvemos al login
  { path: '**', redirectTo: '/login' }
];
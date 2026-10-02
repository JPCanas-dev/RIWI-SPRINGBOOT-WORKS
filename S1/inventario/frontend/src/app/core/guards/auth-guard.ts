// Importamos las herramientas de Angular para controlar las rutas
import { CanActivateFn, Router } from '@angular/router';
// Importamos 'inject' para poder usar el Router dentro de esta función
import { inject } from '@angular/core';

// CanActivateFn significa que esta función decide si una ruta "Se Puede Activar" o no
export const authGuard: CanActivateFn = (route, state) => {
  
  // 1. Inyectamos el enrutador para poder redirigir al usuario si es necesario
  const router = inject(Router);
  
  // 2. Buscamos si existe el token en el almacenamiento del navegador
  const token = localStorage.getItem('accessToken');

  // 3. Tomamos la decisión
  if (token) {
    // Si hay token, retornamos 'true' (Acceso concedido)
    return true;
  } else {
    // Si NO hay token, lo mandamos a la fuerza a la pantalla de login
    router.navigate(['/login']);
    // Y retornamos 'false' (Acceso denegado a la ruta que intentaba entrar)
    return false;
  }
};
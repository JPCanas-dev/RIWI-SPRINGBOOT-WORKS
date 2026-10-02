import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
// 1. Importamos la función para habilitar peticiones HTTP y el inyector de interceptores
import { provideHttpClient, withInterceptors } from '@angular/common/http';
// Importamos nuestro interceptor (el espía que añadirá el token a las peticiones)
import { authInterceptor } from './core/interceptors/auth-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes),
    // 2. Encendemos el cliente HTTP para que toda la app pueda comunicarse con Spring Boot
    // y activamos nuestro interceptor de seguridad para que viaje con cada petición
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
};
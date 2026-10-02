import { HttpInterceptorFn } from '@angular/common/http';

// Un interceptor funcional en Angular toma la petición (req) y la pasa al siguiente paso (next)
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  
  // 1. Buscamos el token donde lo guardaremos cuando el usuario inicie sesión (en el navegador)
  const token = localStorage.getItem('accessToken');

  // 2. Si existe un token, clonamos la petición original y le añadimos el pase VIP
  if (token) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    
    // 3. Dejamos que la petición continúe su viaje, pero ahora lleva el token pegado
    return next(clonedRequest);
  }

  // 4. Si no hay token (por ejemplo, cuando apenas está intentando hacer login), la dejamos pasar normal
  return next(req);
};
// 'export' permite que este modelo se pueda importar y usar en otros archivos (como los servicios).
// 'interface' es solo un "molde" para que TypeScript sepa qué forma tendrán los datos, no ejecuta lógica.

// Así como Token se basa en tu AuthResponse, la interfaz User se basa en lo que tu backend espera recibir cuando 
// alguien intenta hacer login (por lo general, un DTO o un Record en Java llamado algo como LoginRequest o AuthRequest).
export interface User {
  // Obligatorio: El nombre de usuario que el cliente escribe en el formulario.
  username: string;

  // Opcional ('?'): Lo usaremos para enviar la clave al momento de hacer login, 
  // pero tu backend nunca debería devolverte la contraseña de vuelta por seguridad.
  password?: string;

  // Opcional: Útil si tu backend te informa los permisos del usuario (ej. 'ADMIN' o 'USER').
  role?: string;
}
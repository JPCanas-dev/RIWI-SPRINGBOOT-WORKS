// 'export' permite que este modelo se pueda importar y usar en otros archivos (como los servicios).
// 'interface' es solo un "molde" para que TypeScript sepa qué forma tendrán los datos, no ejecuta lógica.

// Deben coincider con el DTO de AuthResponse que es el paquete de datos exacto que viaja por internet 
// desde Spring Boot hasta Angular.
export interface Token {
  // Obligatorio: El token principal que nos dará el backend para autorizar las peticiones.
  accessToken: string;

  // El símbolo '?' significa que este dato es opcional (puede venir o no desde tu Spring Boot).
  refreshToken?: string;

  // Coincide con 'long expiresIn'. En TypeScript, los enteros y decimales son simplemente 'number'
  expiresIn: number;
}
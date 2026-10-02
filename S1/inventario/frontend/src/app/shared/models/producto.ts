// Primero definimos la Categoría para usarla dentro del Producto.
// Esto es necesario porque en Spring Boot usamos @ManyToOne.
export interface Categoria {
  id: number;
  nombre?: string; // Opcional, porque al crear un producto solo enviaremos el ID a Spring Boot
}

// Define la estructura exacta que tu backend de Spring Boot nos va a enviar y recibir
export interface Producto {
  id?: number;       // Opcional (?) porque al crear un producto nuevo aún no tiene ID en la BD
  nombre: string;
  precio: number;
  categoria: Categoria; // Objeto anidado requerido por Spring Boot (ej: { id: 1 })
}
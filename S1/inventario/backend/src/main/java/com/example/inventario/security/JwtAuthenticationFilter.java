package com.example.inventario.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// Esta clase es como el guardia de seguridad de la puerta de un antro:
// Revisa CADA petición HTTP que llega para ver si trae la "pulsera VIP" (El Token).
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Busca el encabezado "Authorization" en la petición de Postman/Angular
        String header = request.getHeader("Authorization");

        // 2. Si no hay token o no empieza con "Bearer ", lo deja pasar (Spring Security lo bloqueará después si la ruta es privada)
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrae solo el código del token (quita la palabra "Bearer ")
        String token = header.substring(7);
        try {
            String username = jwtService.extractUsername(token);

            // 4. Si hay usuario y no está ya autenticado en esta solicitud...
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(username);

                // 5. Valida matemáticamente la firma y expiración del JWT
                if (jwtService.isValid(token, user)) {
                    // 6. Crea la sesión temporal (Authentication) indicándole a Spring que este usuario ES VÁLIDO
                    var authentication = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (RuntimeException ignored) {
            // Si el token es inválido o caducó, simplemente no se crea la sesión
        }

        // Continúa con la cadena de filtros de Spring
        filterChain.doFilter(request, response);
    }
}
package ar.edu.huergo.restaurante.restaurante.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.huergo.restaurante.restaurante.repository.security.UsuarioRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    @SuppressWarnings("unused")
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (H2 console para desarrollo)
                .requestMatchers("/h2-console/**").permitAll()
                // Endpoints de platos - GET permitido para ADMIN y CLIENTE
                .requestMatchers(HttpMethod.GET, "/api/platos/**").hasAnyRole("ADMIN", "CLIENTE")
                .requestMatchers(HttpMethod.POST, "/api/platos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/platos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/platos/**").hasRole("ADMIN")
                // Endpoints de ingredientes - solo ADMIN
                .requestMatchers("/api/ingredientes/**").hasRole("ADMIN")
                // Endpoints de usuarios y roles - solo ADMIN
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/api/roles/**").hasRole("ADMIN")
                // Endpoints de pedidos
                .requestMatchers(HttpMethod.POST, "/api/pedidos/**").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/api/pedidos/**").hasRole("ADMIN")
                // Todos los demás requieren autenticación
                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
                )
                .httpBasic(Customizer.withDefaults())
                // Permitir frames para H2 console
                .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
                );
        return http.build();
    }

    @Bean
    @SuppressWarnings("unused")
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(403);
            response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(java.util.Map.of(
                    "type", "https://http.dev/problems/access-denied",
                    "title", "Acceso denegado",
                    "status", 403,
                    "detail", "No tienes permisos para acceder a este recurso"
            ));

            response.getWriter().write(jsonResponse);
        };
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(java.util.Map.of(
                    "type", "https://http.dev/problems/unauthorized",
                    "title", "No autorizado",
                    "status", 401,
                    "detail", "Credenciales inválidas o faltantes"
            ));

            response.getWriter().write(jsonResponse);
        };
    }

    @Bean
    @SuppressWarnings("unused")
    UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> usuarioRepository.findByUsername(username)
                .map(usuario -> org.springframework.security.core.userdetails.User
                .withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRoles().stream().map(r -> r.getNombre()).toArray(String[]::new))
                .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}

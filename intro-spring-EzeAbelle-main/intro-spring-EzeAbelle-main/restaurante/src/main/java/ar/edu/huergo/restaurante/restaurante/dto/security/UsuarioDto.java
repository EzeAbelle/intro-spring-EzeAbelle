package ar.edu.huergo.restaurante.restaurante.dto.security;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UsuarioDto(
        @NotBlank(message = "El username no puede estar vacío")
        @Email(message = "El username debe ser un email válido")
        String username,
        @NotBlank(message = "La password no puede estar vacía")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{16,}$",
                message = "La password debe tener al menos 16 caracteres, incluyendo mayúsculas, minúsculas, números y caracteres especiales (@$!%*?&)"
        )
        String password,
        Set<String> roles
        ) {

    public UsuarioDto(String username, Set<String> roles) {
        this(username, null, roles);
    }
}

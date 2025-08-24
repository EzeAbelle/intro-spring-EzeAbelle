package ar.edu.huergo.restaurante.restaurante.dto.security;

import jakarta.validation.constraints.NotBlank;

public record RolDto(
        @NotBlank(message = "El nombre del rol no puede estar vacío")
        String nombre
        ) {

}

package ar.edu.huergo.restaurante.restaurante.dto;

import java.util.List;

public record PlatoDto(String nombre, String descripcion, Double precio, List<Long> ingredientesIds) {

}

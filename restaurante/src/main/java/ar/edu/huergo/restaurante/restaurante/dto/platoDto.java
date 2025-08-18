package ar.edu.huergo.restaurante.restaurante.dto;

import java.util.List;
//Los record son clases inmutables que se usan para representar datos
//Autogeneran los metodos equals, hashCode, toString, getters y setters
public record PlatoDto(String nombre, String descripcion, Double precio, List<Long> ingredientesIds) {

}

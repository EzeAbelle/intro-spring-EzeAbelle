package ar.edu.huergo.restaurante.restaurante.dto;

import java.time.LocalDate;
import java.util.List;

public record PedidoDto(
        Long id,
        List<Long> platosIds,
        Double precioTotal,
        LocalDate fecha,
        String clienteUsername
        ) {

}

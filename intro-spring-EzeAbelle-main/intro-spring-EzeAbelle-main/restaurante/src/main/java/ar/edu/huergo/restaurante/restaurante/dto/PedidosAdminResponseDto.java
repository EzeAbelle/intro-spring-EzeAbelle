package ar.edu.huergo.restaurante.restaurante.dto;

import java.util.List;

public record PedidosAdminResponseDto(
        int cantidadDePedidos,
        double totalRecaudado,
        List<PedidoDto> pedidos
        ) {

}

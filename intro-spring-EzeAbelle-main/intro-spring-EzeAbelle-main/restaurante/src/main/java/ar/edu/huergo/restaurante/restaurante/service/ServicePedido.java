package ar.edu.huergo.restaurante.restaurante.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.huergo.restaurante.restaurante.dto.PedidoDto;
import ar.edu.huergo.restaurante.restaurante.dto.PedidosAdminResponseDto;
import ar.edu.huergo.restaurante.restaurante.entity.Pedido;
import ar.edu.huergo.restaurante.restaurante.entity.Plato;
import ar.edu.huergo.restaurante.restaurante.entity.security.Usuario;
import ar.edu.huergo.restaurante.restaurante.repository.PedidoRepository;
import ar.edu.huergo.restaurante.restaurante.repository.PlatoRepository;
import ar.edu.huergo.restaurante.restaurante.repository.security.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ServicePedido {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PlatoRepository platoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public PedidoDto crearPedido(List<Long> platosIds, String clienteUsername) {
        Usuario cliente = usuarioRepository.findByUsername(clienteUsername)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        List<Plato> platos = platoRepository.findAllById(platosIds);
        if (platos.size() != platosIds.size()) {
            throw new EntityNotFoundException("Uno o más platos no fueron encontrados");
        }

        double precioTotal = platos.stream().mapToDouble(Plato::getPrecio).sum();

        Pedido pedido = new Pedido(platos, precioTotal, LocalDate.now(), cliente);
        pedidoRepository.save(pedido);

        return new PedidoDto(
                pedido.getId(),
                platos.stream().map(Plato::getId).collect(Collectors.toList()),
                pedido.getPrecioTotal(),
                pedido.getFecha(),
                pedido.getCliente().getUsername()
        );
    }

    public PedidosAdminResponseDto getPedidosPorFecha(LocalDate fecha) {
        List<Pedido> pedidos = pedidoRepository.findByFecha(fecha);

        List<PedidoDto> pedidosDto = pedidos.stream()
                .map(pedido -> new PedidoDto(
                pedido.getId(),
                pedido.getPlatos().stream().map(Plato::getId).collect(Collectors.toList()),
                pedido.getPrecioTotal(),
                pedido.getFecha(),
                pedido.getCliente().getUsername()
        ))
                .collect(Collectors.toList());

        double totalRecaudado = pedidos.stream().mapToDouble(Pedido::getPrecioTotal).sum();

        return new PedidosAdminResponseDto(pedidos.size(), totalRecaudado, pedidosDto);
    }
}

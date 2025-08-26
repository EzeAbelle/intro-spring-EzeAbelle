package ar.edu.huergo.restaurante.restaurante.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.huergo.restaurante.restaurante.dto.PedidoDto;
import ar.edu.huergo.restaurante.restaurante.dto.PedidosAdminResponseDto;
import ar.edu.huergo.restaurante.restaurante.service.ServicePedido;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private ServicePedido servicePedido;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> crearPedido(@RequestBody List<Long> platosIds, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            PedidoDto pedido = servicePedido.crearPedido(platosIds, userDetails.getUsername());
            return ResponseEntity.ok(pedido);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PedidosAdminResponseDto> getPedidosPorFecha(@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(servicePedido.getPedidosPorFecha(fecha));
    }
}

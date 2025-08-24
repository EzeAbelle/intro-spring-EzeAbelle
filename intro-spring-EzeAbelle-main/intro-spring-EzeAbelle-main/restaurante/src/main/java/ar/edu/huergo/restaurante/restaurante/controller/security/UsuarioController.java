package ar.edu.huergo.restaurante.restaurante.controller.security;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.huergo.restaurante.restaurante.dto.security.UsuarioDto;
import ar.edu.huergo.restaurante.restaurante.entity.security.Usuario;
import ar.edu.huergo.restaurante.restaurante.service.security.ServiceUsuario;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private ServiceUsuario serviceUsuario;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioDto> getUsuarios() {
        return serviceUsuario.getUsuarios();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> getUsuario(@PathVariable Long id) {
        try {
            Optional<Usuario> usuarioOpt = serviceUsuario.getUsuario(id);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Usuario usuario = usuarioOpt.get();
            UsuarioDto usuarioDto = new UsuarioDto(
                    usuario.getUsername(),
                    usuario.getRoles().stream()
                            .map(rol -> rol.getNombre())
                            .collect(java.util.stream.Collectors.toSet())
            );

            return ResponseEntity.ok(usuarioDto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> crearUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        try {
            serviceUsuario.crearUsuario(usuarioDto);
            return ResponseEntity.created(null).body("Usuario creado correctamente");
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDto usuarioDto) {
        try {
            serviceUsuario.actualizarUsuario(id, usuarioDto);
            return ResponseEntity.ok("Usuario actualizado correctamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        try {
            serviceUsuario.eliminarUsuario(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/roles/{nombreRol}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> agregarRol(@PathVariable Long id, @PathVariable String nombreRol) {
        try {
            serviceUsuario.agregarRol(id, nombreRol);
            return ResponseEntity.ok("Rol agregado correctamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/roles/{nombreRol}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removerRol(@PathVariable Long id, @PathVariable String nombreRol) {
        try {
            serviceUsuario.removerRol(id, nombreRol);
            return ResponseEntity.ok("Rol removido correctamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

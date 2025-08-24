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

import ar.edu.huergo.restaurante.restaurante.dto.security.RolDto;
import ar.edu.huergo.restaurante.restaurante.entity.security.Rol;
import ar.edu.huergo.restaurante.restaurante.service.security.ServiceRol;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private ServiceRol serviceRol;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RolDto> getRoles() {
        return serviceRol.getRoles();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolDto> getRol(@PathVariable Long id) {
        try {
            Optional<Rol> rolOpt = serviceRol.getRol(id);
            if (rolOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Rol rol = rolOpt.get();
            return ResponseEntity.ok(new RolDto(rol.getNombre()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> crearRol(@Valid @RequestBody RolDto rolDto) {
        try {
            serviceRol.crearRol(rolDto);
            return ResponseEntity.created(null).body("Rol creado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarRol(@PathVariable Long id, @Valid @RequestBody RolDto rolDto) {
        try {
            serviceRol.actualizarRol(id, rolDto);
            return ResponseEntity.ok("Rol actualizado correctamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarRol(@PathVariable Long id) {
        try {
            serviceRol.eliminarRol(id);
            return ResponseEntity.ok("Rol eliminado correctamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

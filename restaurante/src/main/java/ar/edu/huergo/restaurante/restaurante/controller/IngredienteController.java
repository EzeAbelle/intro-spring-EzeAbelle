package ar.edu.huergo.restaurante.restaurante.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.huergo.restaurante.restaurante.dto.IngredienteDto;
import ar.edu.huergo.restaurante.restaurante.entity.Ingrediente;
import ar.edu.huergo.restaurante.restaurante.service.ServiceIngrediente;

@RestController
@RequestMapping("/ingrediente")
public class IngredienteController {
    @Autowired
    private ServiceIngrediente serviceIngrediente;

    @GetMapping
    public List<IngredienteDto> getIngredientes() {
        return this.serviceIngrediente.getIngredientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredienteDto> getIngrediente(@PathVariable Long id) {
        try {
            Optional<Ingrediente> IngredienteOpt = this.serviceIngrediente.getIngrediente(id);
            if (IngredienteOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Ingrediente Ingrediente = IngredienteOpt.get();
            return ResponseEntity.ok(new IngredienteDto(Ingrediente.getNombre()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> crearIngrediente(@RequestBody IngredienteDto IngredienteDto) {
        try {
            this.serviceIngrediente.crearIngrediente(IngredienteDto);
            return ResponseEntity.created(null).body("Ingrediente creado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping({"/{id}", ""})
    public ResponseEntity<String> actualizarIngrediente(
            @PathVariable(value = "id", required = false) Long pathId,
            @RequestParam(value = "id", required = false) Long paramId,
            @RequestBody IngredienteDto IngredienteDto) {
        Long id = (pathId != null) ? pathId : paramId;
        if (id == null) {
            return ResponseEntity.badRequest().body("Debe proporcionar el id del Ingrediente");
        }
        try {
            this.serviceIngrediente.actualizarIngrediente(id, IngredienteDto);
            return ResponseEntity.ok("Ingrediente actualizado correctamente");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarIngrediente(@PathVariable Long id) {
        try {
            this.serviceIngrediente.eliminarIngrediente(id);
            return ResponseEntity.ok("Ingrediente eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

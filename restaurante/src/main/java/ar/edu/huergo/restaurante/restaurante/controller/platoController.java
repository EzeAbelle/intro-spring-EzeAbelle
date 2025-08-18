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

import ar.edu.huergo.restaurante.restaurante.dto.PlatoDto;
import ar.edu.huergo.restaurante.restaurante.entity.Plato;
import ar.edu.huergo.restaurante.restaurante.service.ServicePlato;

@RestController
@RequestMapping("/plato")
public class PlatoController {

    @Autowired
    private ServicePlato servicePlato;

    @GetMapping
    public List<PlatoDto> getPlatos() {
        return this.servicePlato.getPlatos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatoDto> getPlato(@PathVariable Long id) {
        try {
            Optional<Plato> platoOpt = this.servicePlato.getPlato(id);
            if (platoOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Plato plato = platoOpt.get();
            return ResponseEntity.ok(new PlatoDto(plato.getNombre(), plato.getDescripcion(), plato.getPrecio(), plato.getIngredientesIds()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> crearPlato(@RequestBody PlatoDto platoDto) {
        try {
            this.servicePlato.crearPlato(platoDto);
            return ResponseEntity.created(null).body("Plato creado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping({"/{id}", ""})
    public ResponseEntity<String> actualizarPlato(
            @PathVariable(value = "id", required = false) Long pathId,
            @RequestParam(value = "id", required = false) Long paramId,
            @RequestBody PlatoDto platoDto) {
        Long id = (pathId != null) ? pathId : paramId;
        if (id == null) {
            return ResponseEntity.badRequest().body("Debe proporcionar el id del plato");
        }
        try {
            this.servicePlato.actualizarPlato(id, platoDto);
            return ResponseEntity.ok("Plato actualizado correctamente");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPlato(@PathVariable Long id) {
        try {
            this.servicePlato.eliminarPlato(id);
            return ResponseEntity.ok("Plato eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

package ar.edu.huergo.restaurante.restaurante.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ar.edu.huergo.restaurante.restaurante.dto.IngredienteDto;
import ar.edu.huergo.restaurante.restaurante.entity.Ingrediente;
import ar.edu.huergo.restaurante.restaurante.repository.IngredienteRepository;

@Service
public class ServiceIngrediente {
    @Autowired
    private IngredienteRepository ingredienteRepository;

    public List<IngredienteDto> getIngredientes() {
        return ((List<Ingrediente>) this.ingredienteRepository.findAll())
            .stream()
            .map(Ingrediente -> new IngredienteDto(Ingrediente.getNombre()))
            .toList();
    }

    public Optional<Ingrediente> getIngrediente(Long id) {
        return this.ingredienteRepository.findById(id);
    }

    public void crearIngrediente(IngredienteDto IngredienteDto) {
        this.ingredienteRepository.save(new Ingrediente(IngredienteDto.nombre()));
    }

    public void actualizarIngrediente(Long id, IngredienteDto IngredienteDto) throws NotFoundException {
        Ingrediente Ingrediente = this.ingredienteRepository.findById(id).orElseThrow(() -> new NotFoundException());
        Ingrediente.setNombre(IngredienteDto.nombre());
        this.ingredienteRepository.save(Ingrediente);
    }

    public void eliminarIngrediente(Long id) {
        this.ingredienteRepository.deleteById(id);
    }
}

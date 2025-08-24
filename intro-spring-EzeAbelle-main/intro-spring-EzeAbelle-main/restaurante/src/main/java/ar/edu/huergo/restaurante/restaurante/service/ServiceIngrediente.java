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
        return this.ingredienteRepository.findAll()
                .stream()
                .map(ingrediente -> new IngredienteDto(ingrediente.getNombre()))
                .toList();
    }

    public Optional<Ingrediente> getIngrediente(Long id) {
        return this.ingredienteRepository.findById(id);
    }

    public void crearIngrediente(IngredienteDto ingredienteDto) {
        this.ingredienteRepository.save(new Ingrediente(ingredienteDto.nombre()));
    }

    public void actualizarIngrediente(Long id, IngredienteDto ingredienteDto) throws NotFoundException {
        Ingrediente ingrediente = this.ingredienteRepository.findById(id).orElseThrow(() -> new NotFoundException());
        ingrediente.setNombre(ingredienteDto.nombre());
        this.ingredienteRepository.save(ingrediente);
    }

    public void eliminarIngrediente(Long id) {
        this.ingredienteRepository.deleteById(id);
    }
}

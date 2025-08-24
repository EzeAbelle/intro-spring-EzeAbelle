package ar.edu.huergo.restaurante.restaurante.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ar.edu.huergo.restaurante.restaurante.dto.PlatoDto;
import ar.edu.huergo.restaurante.restaurante.entity.Plato;
import ar.edu.huergo.restaurante.restaurante.repository.PlatoRepository;

@Service
public class ServicePlato {

    @Autowired
    private PlatoRepository platoRepository;

    public List<PlatoDto> getPlatos() {
        return this.platoRepository.findAll()
                .stream()
                .map(plato -> new PlatoDto(plato.getNombre(), plato.getDescripcion(), plato.getPrecio(), plato.getIngredientesIds()))
                .toList();
    }

    public Optional<Plato> getPlato(Long id) {
        return this.platoRepository.findById(id);
    }

    public void crearPlato(PlatoDto platoDto) {
        Plato plato = new Plato(platoDto.nombre(), platoDto.descripcion(), platoDto.precio(), platoDto.ingredientesIds());
        this.platoRepository.save(plato);
    }

    public void actualizarPlato(Long id, PlatoDto platoDto) throws NotFoundException {
        Plato plato = this.platoRepository.findById(id).orElseThrow(() -> new NotFoundException());
        plato.setNombre(platoDto.nombre());
        plato.setDescripcion(platoDto.descripcion());
        plato.setPrecio(platoDto.precio());
        plato.setIngredientesIds(platoDto.ingredientesIds());
        this.platoRepository.save(plato);
    }

    public void eliminarPlato(Long id) {
        this.platoRepository.deleteById(id);
    }
}

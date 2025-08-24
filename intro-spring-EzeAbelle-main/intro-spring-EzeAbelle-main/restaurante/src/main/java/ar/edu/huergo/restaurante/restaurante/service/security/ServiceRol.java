package ar.edu.huergo.restaurante.restaurante.service.security;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.huergo.restaurante.restaurante.dto.security.RolDto;
import ar.edu.huergo.restaurante.restaurante.entity.security.Rol;
import ar.edu.huergo.restaurante.restaurante.repository.security.RolRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceRol {

    @Autowired
    private RolRepository rolRepository;

    public List<RolDto> getRoles() {
        return rolRepository.findAll()
                .stream()
                .map(rol -> new RolDto(rol.getNombre()))
                .toList();
    }

    public Optional<Rol> getRol(Long id) {
        return rolRepository.findById(id);
    }

    public Optional<Rol> getRolByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    public void crearRol(RolDto rolDto) {
        if (rolRepository.existsByNombre(rolDto.nombre())) {
            throw new IllegalArgumentException("Ya existe un rol con ese nombre");
        }

        Rol rol = new Rol(rolDto.nombre());
        rolRepository.save(rol);
    }

    public void actualizarRol(Long id, RolDto rolDto) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + id));

        // Verificar si el nuevo nombre ya existe (excepto para el rol actual)
        if (!rol.getNombre().equals(rolDto.nombre())
                && rolRepository.existsByNombre(rolDto.nombre())) {
            throw new IllegalArgumentException("Ya existe un rol con ese nombre");
        }

        rol.setNombre(rolDto.nombre());
        rolRepository.save(rol);
    }

    public void eliminarRol(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new EntityNotFoundException("Rol no encontrado con id: " + id);
        }
        rolRepository.deleteById(id);
    }
}

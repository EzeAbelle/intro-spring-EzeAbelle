package ar.edu.huergo.restaurante.restaurante.service.security;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ar.edu.huergo.restaurante.restaurante.dto.security.UsuarioDto;
import ar.edu.huergo.restaurante.restaurante.entity.security.Rol;
import ar.edu.huergo.restaurante.restaurante.entity.security.Usuario;
import ar.edu.huergo.restaurante.restaurante.repository.security.RolRepository;
import ar.edu.huergo.restaurante.restaurante.repository.security.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioDto> getUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<Usuario> getUsuario(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> getUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public void crearUsuario(UsuarioDto usuarioDto) {
        if (usuarioRepository.existsByUsername(usuarioDto.username())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDto.username());
        usuario.setPassword(passwordEncoder.encode(usuarioDto.password()));

        // Asignar roles
        if (usuarioDto.roles() != null && !usuarioDto.roles().isEmpty()) {
            Set<Rol> roles = usuarioDto.roles().stream()
                    .map(nombreRol -> rolRepository.findByNombre(nombreRol)
                    .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + nombreRol)))
                    .collect(Collectors.toSet());
            usuario.setRoles(roles);
        }

        usuarioRepository.save(usuario);
    }

    public void actualizarUsuario(Long id, UsuarioDto usuarioDto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

        // Verificar si el nuevo username ya existe (excepto para el usuario actual)
        if (!usuario.getUsername().equals(usuarioDto.username())
                && usuarioRepository.existsByUsername(usuarioDto.username())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        usuario.setUsername(usuarioDto.username());

        // Solo actualizar password si se proporciona una nueva
        if (usuarioDto.password() != null && !usuarioDto.password().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDto.password()));
        }

        // Actualizar roles
        if (usuarioDto.roles() != null) {
            Set<Rol> roles = usuarioDto.roles().stream()
                    .map(nombreRol -> rolRepository.findByNombre(nombreRol)
                    .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + nombreRol)))
                    .collect(Collectors.toSet());
            usuario.setRoles(roles);
        }

        usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public void agregarRol(Long usuarioId, String nombreRol) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + nombreRol));

        usuario.addRol(rol);
        usuarioRepository.save(usuario);
    }

    public void removerRol(Long usuarioId, String nombreRol) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + nombreRol));

        usuario.removeRol(rol);
        usuarioRepository.save(usuario);
    }

    private UsuarioDto convertToDto(Usuario usuario) {
        Set<String> rolesNames = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .collect(Collectors.toSet());

        return new UsuarioDto(usuario.getUsername(), rolesNames);
    }
}

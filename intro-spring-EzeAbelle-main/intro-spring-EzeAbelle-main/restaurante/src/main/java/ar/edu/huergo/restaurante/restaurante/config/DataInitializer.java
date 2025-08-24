package ar.edu.huergo.restaurante.restaurante.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ar.edu.huergo.restaurante.restaurante.entity.security.Rol;
import ar.edu.huergo.restaurante.restaurante.entity.security.Usuario;
import ar.edu.huergo.restaurante.restaurante.repository.security.RolRepository;
import ar.edu.huergo.restaurante.restaurante.repository.security.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        if (!rolRepository.existsByNombre("ADMIN")) {
            Rol adminRole = new Rol("ADMIN");
            rolRepository.save(adminRole);
        }

        if (!rolRepository.existsByNombre("CLIENTE")) {
            Rol clienteRole = new Rol("CLIENTE");
            rolRepository.save(clienteRole);
        }

        // Crear usuario admin por defecto si no existe
        if (!usuarioRepository.existsByUsername("admin@restaurante.com")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin@restaurante.com");
            // Password: AdminRestaurante123!
            admin.setPassword(passwordEncoder.encode("AdminRestaurante123!"));

            Rol adminRole = rolRepository.findByNombre("ADMIN").orElseThrow();
            admin.addRol(adminRole);

            usuarioRepository.save(admin);
            System.out.println("Usuario admin creado: admin@restaurante.com / AdminRestaurante123!");
        }

        // Crear usuario cliente por defecto si no existe
        if (!usuarioRepository.existsByUsername("cliente@test.com")) {
            Usuario cliente = new Usuario();
            cliente.setUsername("cliente@test.com");
            // Password: ClienteTest12345!
            cliente.setPassword(passwordEncoder.encode("ClienteTest12345!"));

            Rol clienteRole = rolRepository.findByNombre("CLIENTE").orElseThrow();
            cliente.addRol(clienteRole);

            usuarioRepository.save(cliente);
            System.out.println("Usuario cliente creado: cliente@test.com / ClienteTest12345!");
        }
    }
}

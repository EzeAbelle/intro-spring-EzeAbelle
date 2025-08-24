package ar.edu.huergo.restaurante.restaurante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.huergo.restaurante.restaurante.entity.Plato;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {
}

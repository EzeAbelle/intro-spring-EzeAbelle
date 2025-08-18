package ar.edu.huergo.restaurante.restaurante.repository;

import org.springframework.data.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.huergo.restaurante.restaurante.entity.Ingrediente;

//CrudRepository es una interfaz que proporciona metodos para CRUD (Create, Read, Update, Delete)
//<Empleado, Long> indica que el repositorio se encarga de los empleados y que la clave primaria es Long
@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
    
}
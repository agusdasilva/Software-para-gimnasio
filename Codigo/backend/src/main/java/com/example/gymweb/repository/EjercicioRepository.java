package com.example.gymweb.repository;

import com.example.gymweb.model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {

    Optional<Ejercicio> findByNombreIgnoreCase(String nombre);
    Optional<List<Ejercicio>> findAllByNombreContainingIgnoreCase(String nombre);
    Optional<List<Ejercicio>> findAllByGrupoMuscularIgnoreCase(String grupoMuscular);
}

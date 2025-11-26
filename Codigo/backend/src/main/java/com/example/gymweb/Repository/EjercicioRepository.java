package com.example.gymweb.Repository;


import com.example.gymweb.model.Ejercicio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {
    Optional<Ejercicio> findByNombreIgnoreCase(String nombre);

    Optional<List<Ejercicio>> findAllByNombreContainingIgnoreCase(String nombre);

    Optional<List<Ejercicio>> findAllByGrupoMuscularIgnoreCase(String grupoMuscular);
}

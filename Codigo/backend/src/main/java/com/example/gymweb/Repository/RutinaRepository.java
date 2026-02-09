package com.example.gymweb.Repository;


import com.example.gymweb.model.Rutina;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Integer> {
    Optional<Rutina> findByNombreIgnoreCase(String nombre);
    List<Rutina> findByCreadorId(int creadorId);
    List<Rutina> findByEsGlobalTrue();
}

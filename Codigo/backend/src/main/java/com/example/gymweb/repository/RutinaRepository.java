package com.example.gymweb.repository;

import com.example.gymweb.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Integer> {
    Optional<Rutina> findByNombreIgnoreCase(String nombre);
}

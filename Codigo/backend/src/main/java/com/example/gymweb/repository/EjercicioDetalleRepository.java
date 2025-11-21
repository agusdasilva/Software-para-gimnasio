package com.example.gymweb.repository;

import com.example.gymweb.model.EjercicioDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjercicioDetalleRepository extends JpaRepository<EjercicioDetalle, Integer> {
}

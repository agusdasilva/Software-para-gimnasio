package com.example.gymweb.repository;

import com.example.gymweb.model.RutinaDetalle;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutinaDetalleRepository extends JpaRepository<RutinaDetalle, Integer> {
}

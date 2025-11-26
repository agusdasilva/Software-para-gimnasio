package com.example.gymweb.Repository;


import com.example.gymweb.model.RutinaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutinaDetalleRepository extends JpaRepository<RutinaDetalle, Integer> {
}
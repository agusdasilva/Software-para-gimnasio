package com.example.gymweb.repository;

import com.example.gymweb.model.MensajeClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeClaseRepository extends JpaRepository<MensajeClase, Integer> {
}

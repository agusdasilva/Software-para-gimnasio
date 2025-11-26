package com.example.gymweb.Repository;


import com.example.gymweb.model.MensajeClase;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeClaseRepository extends JpaRepository<MensajeClase, Integer> {
    Optional<MensajeClase> findByClaseId(int id);
}
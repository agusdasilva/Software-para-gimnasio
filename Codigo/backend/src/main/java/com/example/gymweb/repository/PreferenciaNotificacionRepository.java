package com.example.gymweb.repository;

import com.example.gymweb.model.PrefereciaNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenciaNotificacionRepository extends JpaRepository<PrefereciaNotificacion, Integer> {
}

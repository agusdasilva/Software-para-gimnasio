package com.example.gymweb.Repository;

import com.example.gymweb.model.PreferenciaNotificacion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenciaNotificacionRepository extends JpaRepository<PreferenciaNotificacion, Integer> {
    Optional<PreferenciaNotificacion> findByUsuario_Id(int idUsuario);
}
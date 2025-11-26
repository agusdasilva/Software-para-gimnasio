package com.example.gymweb.Repository;

import com.example.gymweb.model.Notificacion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    Optional<Notificacion> findByUsuario_Id(int idUsuario);
}
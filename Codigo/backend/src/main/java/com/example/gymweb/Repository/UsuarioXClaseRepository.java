package com.example.gymweb.Repository;

import com.example.gymweb.model.UsuarioXClase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioXClaseRepository extends JpaRepository<UsuarioXClase, Integer> {
    boolean existsByUsuarioIdAndClaseId(int idUsuario, int idClase);

    Optional<UsuarioXClase> findByUsuarioIdAndClaseId(int idUsuario, int idClase);

    List<UsuarioXClase> findByClaseId(int claseId);
}

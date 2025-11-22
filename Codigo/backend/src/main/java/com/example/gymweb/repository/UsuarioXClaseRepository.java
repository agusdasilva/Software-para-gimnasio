package com.example.gymweb.repository;

import com.example.gymweb.model.UsuarioXClase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioXClaseRepository extends JpaRepository<UsuarioXClase, Integer> {
    boolean existsByUsuarioIdAndClaseId(int idUsuario, int idClase);
    List<UsuarioXClase> findByClaseId(int claseId);
}

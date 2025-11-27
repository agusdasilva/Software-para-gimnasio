package com.example.gymweb.Repository;


import com.example.gymweb.model.PerfilUsuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Integer> {
    Optional<PerfilUsuario> findByUsuarioId(Integer usuarioId);
}

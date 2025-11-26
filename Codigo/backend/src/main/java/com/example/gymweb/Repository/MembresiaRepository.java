package com.example.gymweb.Repository;

import com.example.gymweb.model.Membresia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembresiaRepository extends JpaRepository<Membresia, Integer> {

    Optional<Membresia> findFirstByUsuarioIdOrderByFechaFinDesc(int usuarioId);

}
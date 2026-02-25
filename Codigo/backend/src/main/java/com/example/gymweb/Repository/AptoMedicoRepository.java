package com.example.gymweb.Repository;

import com.example.gymweb.model.AptoEstado;
import com.example.gymweb.model.AptoMedico;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AptoMedicoRepository extends JpaRepository<AptoMedico, Integer> {
    List<AptoMedico> findByUsuario_IdOrderByFechaSubidaDesc(Integer usuarioId);
    List<AptoMedico> findByEstadoOrderByFechaSubidaAsc(AptoEstado estado);

    @Query("select a from AptoMedico a where a.estado = :estado and (a.fechaVencimiento is null or a.fechaVencimiento >= :hoy)")
    List<AptoMedico> findVigentes(AptoEstado estado, LocalDate hoy);
}

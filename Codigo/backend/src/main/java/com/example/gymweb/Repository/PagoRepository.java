package com.example.gymweb.Repository;

import com.example.gymweb.model.Pago;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    List<Pago> findByMembresiaId(int membresiaId);
}
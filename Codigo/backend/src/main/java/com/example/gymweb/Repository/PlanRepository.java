package com.example.gymweb.Repository;

import com.example.gymweb.model.Plan;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    Optional<Plan> findByNombreIgnoreCase(String nombre);
}
package com.example.gymweb.Repository;

import org.springframework.stereotype.Repository;


import com.example.gymweb.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Integer> {
}

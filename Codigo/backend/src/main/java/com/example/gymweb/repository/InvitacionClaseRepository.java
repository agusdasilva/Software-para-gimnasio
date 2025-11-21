package com.example.gymweb.repository;

import com.example.gymweb.model.InvitacionClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitacionClaseRepository extends JpaRepository<InvitacionClase, Integer> {
}

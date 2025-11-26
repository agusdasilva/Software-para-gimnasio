package com.example.gymweb.Repository;


import com.example.gymweb.model.InvitacionClase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitacionClaseRepository extends JpaRepository<InvitacionClase, Integer> {
    List<InvitacionClase> findByUsuarioClase_Usuario_Id(int idUsuario);
}
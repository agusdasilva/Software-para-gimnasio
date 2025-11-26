package com.example.gymweb.model;

<<<<<<< Updated upstream
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table (name = "invitacion_clase")
public class InvitacionClase {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_invitacion")
    private int id;

    @ManyToOne
    @JoinColumn(name = "usu_clase_id")
    private UsuarioXClase usuarioClase;

=======
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "invitacion_clase"
)
public class InvitacionClase {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_invitacion"
    )
    private int id;
    @ManyToOne
    @JoinColumn(
            name = "usu_clase_id"
    )
    private UsuarioXClase usuarioClase;
>>>>>>> Stashed changes
    private EstadoInvitacion estado;
    private LocalDateTime fecha;

    public InvitacionClase(int id, UsuarioXClase usuarioClase, EstadoInvitacion estado, LocalDateTime fecha) {
        this.id = id;
        this.usuarioClase = usuarioClase;
        this.estado = estado;
        this.fecha = fecha;
    }

    public InvitacionClase() {
    }

    public int getId() {
<<<<<<< Updated upstream
        return id;
=======
        return this.id;
>>>>>>> Stashed changes
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsuarioXClase getUsuarioClase() {
<<<<<<< Updated upstream
        return usuarioClase;
=======
        return this.usuarioClase;
>>>>>>> Stashed changes
    }

    public void setUsuarioClase(UsuarioXClase usuarioClase) {
        this.usuarioClase = usuarioClase;
    }

    public EstadoInvitacion getEstado() {
<<<<<<< Updated upstream
        return estado;
=======
        return this.estado;
>>>>>>> Stashed changes
    }

    public void setEstado(EstadoInvitacion estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
<<<<<<< Updated upstream
        return fecha;
=======
        return this.fecha;
>>>>>>> Stashed changes
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}

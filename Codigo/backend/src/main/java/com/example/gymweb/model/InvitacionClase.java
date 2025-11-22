package com.example.gymweb.model;

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
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsuarioXClase getUsuarioClase() {
        return usuarioClase;
    }

    public void setUsuarioClase(UsuarioXClase usuarioClase) {
        this.usuarioClase = usuarioClase;
    }

    public EstadoInvitacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoInvitacion estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}

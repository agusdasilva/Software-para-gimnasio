package com.example.gymweb.model;

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
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsuarioXClase getUsuarioClase() {
        return this.usuarioClase;
    }

    public void setUsuarioClase(UsuarioXClase usuarioClase) {
        this.usuarioClase = usuarioClase;
    }

    public EstadoInvitacion getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoInvitacion estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}

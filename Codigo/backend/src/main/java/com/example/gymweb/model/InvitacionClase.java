package com.example.gymweb.model;

import jakarta.persistence.*;

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

    private Estado estado;
    private Date fecha;

    public InvitacionClase(int id, UsuarioXClase usuarioClase, Estado estado, Date fecha) {
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}

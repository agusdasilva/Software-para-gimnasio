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
    @JoinColumn (name = "usu_clase_id")
    private Usuario usuario_clase_id;

    private Estado estado;
    private Date fecha;

    public InvitacionClase(int id, Usuario usuario_clase_id, Estado estado, Date fecha) {
        this.id = id;
        this.usuario_clase_id = usuario_clase_id;
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

    public Usuario getUsuario_clase_id() {
        return usuario_clase_id;
    }

    public void setUsuario_clase_id(Usuario usuario_clase_id) {
        this.usuario_clase_id = usuario_clase_id;
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

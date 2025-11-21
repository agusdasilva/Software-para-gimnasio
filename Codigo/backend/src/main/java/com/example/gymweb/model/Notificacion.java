package com.example.gymweb.model;

import jakarta.persistence.*;
import org.w3c.dom.Text;

import java.util.Date;

@Entity
@Table (name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private int id;

    @ManyToOne
    @JoinColumn (name = "usuario_id")
    private Usuario usuario;
    @Lob
    private String mensaje;
    private Date fecha;
    @Column(name = "leida")
    private boolean leida;

    public Notificacion(int id, Usuario usuario, String mensaje, Date fecha, boolean leida) {
        this.id = id;
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leida = leida;
    }

    public Notificacion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }
}


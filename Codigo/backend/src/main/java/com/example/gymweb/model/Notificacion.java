package com.example.gymweb.model;

import jakarta.persistence.*;
import org.w3c.dom.Text;

import java.util.Date;

@Entity
@Table (name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id_notificacion")
    private int id;

    @ManyToOne
    @JoinColumn (name = "usuario_id")
    private Usuario usuario;

    private Text mensaje;
    private Date fecha;
    private int leido;

    public Notificacion(int id, Usuario usuario, Text mensaje, Date fecha, int leido) {
        this.id = id;
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leido = leido;
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

    public Text getMensaje() {
        return mensaje;
    }

    public void setMensaje(Text mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getLeido() {
        return leido;
    }

    public void setLeido(int leido) {
        this.leido = leido;
    }
}

